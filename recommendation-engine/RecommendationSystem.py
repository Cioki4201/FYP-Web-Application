import ast
import pandas as pd
import sys
import os
import numpy as np
import pickle
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from functools import lru_cache

WEIGHTS = {
    'genre_weight': 3,
    'involved_companies_weight': 1,
    'keywords_weight': 1,
    'platforms_weight': 1,
    'player_perspectives_weight': 1
}

def parse_list_of_dicts(x):
    return ast.literal_eval(x) if isinstance(x, str) else x

def join_names(lst):
    return ' '.join([item.get('name', '') for item in lst]) if isinstance(lst, list) and all(isinstance(item, dict) for item in lst) else ''


@lru_cache(maxsize=None)
def get_games_df():
    if not os.path.exists('preprocessed_game_data.csv'):
        raw_games_df = pd.read_csv('game_data.csv')
        preprocessed_games_df = preprocess_data(raw_games_df)
        preprocessed_games_df.to_csv('preprocessed_game_data.csv', index=False)

    return pd.read_csv('preprocessed_game_data.csv')

def preprocess_data(games_df):
    list_columns = ['involved_companies', 'keywords', 'platforms', 'player_perspectives', 'genres']

    for col in list_columns:
        games_df[col] = games_df[col].apply(parse_list_of_dicts)
        if col != 'genres':
            games_df[f"{col}_str"] = games_df[col].apply(join_names)
            games_df[f"{col}_weighted"] = games_df[f"{col}_str"] * WEIGHTS[f"{col}_weight"]

    games_df['summary'].fillna('', inplace=True)
    games_df['genres_str'] = games_df['genres'].apply(join_names)
    games_df['summary_and_genres'] = (games_df['summary'] + ' ' + (games_df['genres_str'] * WEIGHTS['genre_weight']))

    for col in list_columns:
        if col != 'genres':
            games_df['summary_and_genres'] = games_df['summary_and_genres'] + ' ' + games_df[f"{col}_weighted"]

    return games_df


def precompute_similarity_scores():
    games_df = get_games_df()
    vectorizer = TfidfVectorizer(stop_words='english', max_features=13000)
    summary_genre_matrix = vectorizer.fit_transform(games_df['summary_and_genres'])

    similarity_scores = cosine_similarity(summary_genre_matrix)

    with open('similarity_scores.pkl', 'wb') as f:
        pickle.dump(similarity_scores, f)

    return similarity_scores


@lru_cache(maxsize=None)
def get_similarity_scores():
    if not os.path.exists('similarity_scores.pkl'):
        similarity_scores = precompute_similarity_scores()
    else:
        with open('similarity_scores.pkl', 'rb') as f:
            similarity_scores = pickle.load(f)

    return similarity_scores


def generate_features(user_games):
    games_df = get_games_df()
    user_games_df = games_df[games_df['id'].isin(user_games)]
    remaining_games = games_df[~games_df['name'].isin(user_games)]
    sampled_games = pd.concat([user_games_df, remaining_games.head(15000 - len(user_games))], ignore_index=True)

    vectorizer = TfidfVectorizer(stop_words='english', max_features=13000)
    summary_genre_matrix = vectorizer.fit_transform(sampled_games['summary_and_genres'])

    return summary_genre_matrix, sampled_games

def recommend_games(user_game_ids, top_n=5, min_rating=60, min_rating_count=20):
    games_df = get_games_df()
    similarity_scores = get_similarity_scores()

    user_game_indices = games_df[games_df['id'].isin(user_game_ids)].index

    avg_similarity_scores = similarity_scores[user_game_indices].mean(axis=0)

    mask = (~games_df['id'].isin(user_game_ids)) & (games_df['total_rating'] >= min_rating) & (games_df['total_rating_count'] >= min_rating_count)
    filtered_similarity_scores = avg_similarity_scores[mask]

    most_similar_indices = np.argpartition(-filtered_similarity_scores, top_n)[:top_n]
    most_similar_indices = most_similar_indices[np.argsort(-filtered_similarity_scores[most_similar_indices])]

    recommended_games = games_df.loc[mask].iloc[most_similar_indices]

    return recommended_games[['name', 'id', 'total_rating', 'total_rating_count']]



def main(user_games):
    recommended_games = recommend_games(user_games, top_n=8)
    print('Recommended games:')
    print(recommended_games)

    return recommended_games['id'].tolist()

if __name__ == '__main__':
    user_games_list = list(map(int, sys.argv[1].split(",")))
    main(user_games_list)