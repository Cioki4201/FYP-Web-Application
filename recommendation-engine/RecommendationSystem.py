import ast
import pandas as pd
import sys
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity


def preprocess_data(games_df):
    games_df['summary'].fillna('', inplace=True)

    for col in ['genres', 'involved_companies', 'platforms']:
        games_df[col] = games_df[col].apply(lambda x: ast.literal_eval(x) if isinstance(x, str) else x)

    games_df['genres_str'] = games_df['genres'].apply(
        lambda x: ' '.join([genre['name'] for genre in x]) if isinstance(x, list) and all(
            isinstance(genre, dict) for genre in x) else '')

    games_df['summary_and_genres'] = games_df['summary'] + ' ' + games_df['genres_str']

    return games_df

# Preprocess the data and save to a file
# games_df = pd.read_csv('game_data.csv')
# games_df = preprocess_data(games_df)
# games_df.to_csv('preprocessed_game_data.csv', index=False)


def generate_features(games_df):
    vectorizer = TfidfVectorizer(stop_words='english', max_features=20000)
    summary_genre_matrix = vectorizer.fit_transform(games_df['summary_and_genres'])

    return summary_genre_matrix


def recommend_games(user_game_ids, games_df, summary_genre_matrix, top_n=5, min_rating=70, min_rating_count=20):
    user_game_indices = games_df[games_df['id'].isin(user_game_ids)].index
    user_game_matrix = summary_genre_matrix[user_game_indices]

    similarity_scores = cosine_similarity(user_game_matrix, summary_genre_matrix)
    similarity_scores = similarity_scores.mean(axis=0)

    # Create a mask to exclude user_games, games with rating less than min_rating, and games with rating count less than min_rating_count from the recommendations
    mask = (~games_df['id'].isin(user_game_ids)) & (games_df['total_rating'] >= min_rating) & (games_df['total_rating_count'] >= min_rating_count)
    filtered_similarity_scores = similarity_scores[mask]

    most_similar_indices = (-filtered_similarity_scores).argsort()[:top_n]
    most_similar_indices = games_df[mask].iloc[most_similar_indices].index

    return games_df.loc[most_similar_indices, ['name', 'id', 'total_rating', 'total_rating_count']]




def main(user_games):
    games_df = pd.read_csv('preprocessed_game_data.csv')

    # Data has been already preprocessed and saved to game_data.csv, so we can now skip the preprocessing step
    # games_df = preprocess_data(games_df)

    games_df = games_df.sample(frac=1, random_state=42).reset_index(drop=True)

    # Extract user_games from the games_df
    user_games_df = games_df[games_df['id'].isin(user_games)]

    # Exclude user_games from the games_df
    remaining_games = games_df[~games_df['name'].isin(user_games)]

    # Sample the remaining_games
    sampled_games = remaining_games.head(10000 - len(user_games))

    # Concatenate user_games_df and the sampled_games
    sampled_games = pd.concat([user_games_df, sampled_games], ignore_index=True)

    # Generate features for the sampled_games
    summary_genre_matrix = generate_features(sampled_games)

    # Recommend games based on the user_games
    recommended_games = recommend_games(user_games, sampled_games, summary_genre_matrix, top_n=8)
    print('Recommended games:')
    print(recommended_games)

    # store game IDs in a list
    game_ids = recommended_games['id'].tolist()
    return game_ids


if __name__ == '__main__':
    user_games_list = list(map(int, sys.argv[1].split(",")))
    main(user_games_list)