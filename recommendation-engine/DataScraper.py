import requests
import pandas as pd
import time

def fetch_games(offset):
    url = 'https://api.igdb.com/v4/games'
    query = f'''
    fields id, name, total_rating, total_rating_count, category, genres.name, platforms.name, involved_companies.company.name, summary;
    where release_dates.date > 978307200 & total_rating != null;
    sort release_dates.date asc;
    limit 500;
    offset {offset};
    '''

    response = requests.post(url, data=query, headers=headers)

    if response.status_code == 200:
        return response.json()
    else:
        print(f'Error: {response.status_code} : {response.text}')
        return None

api_key = '3x7wa4db826lpwqiithvvvzg77ebkg'
headers = {
    'Accept': 'application/json',
    'Client-ID': "h8e4zy4n8gdygc69d4kfz4qsn9ymn3",
    'Authorization': f'Bearer {api_key}',
}

all_games = []
offset = 0
batch_size = 500

while True:
    print(f'Fetching games with offset {offset}... Fetched {len(all_games)} games so far.')
    # Print the head of the all_games list every 10 batches
    if offset % (batch_size * 10) == 0:
        print(all_games[:5])
    games = fetch_games(offset)

    if not games:
        break

    if len(games) == 0:
        print('No more games to fetch.')
        break

    all_games.extend(games)
    offset += batch_size
    time.sleep(0.25)

games_df = pd.DataFrame(all_games)

# Filter out DLCs (category == 1)
games_df = games_df[games_df['category'] != 1]

games_df.to_csv('game_data.csv', index=False)
print('Game data without DLCs and games without ratings saved to game_data.csv.')
