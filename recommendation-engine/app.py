import time

from flask import Flask, request, jsonify
import RecommendationSystem

app = Flask(__name__)


@app.route('/recommend_games', methods=['POST'])
def recommend_games():
    # Get time to calculate the time taken to recommend games
    start_time = time.time()

    user_games = request.json['user_games']
    recommended_games = RecommendationSystem.main(user_games)

    # Calculate time taken to recommend games
    time_taken = time.time() - start_time
    print(f'Time taken to recommend games: {time_taken} seconds')

    return jsonify(recommended_games)


if __name__ == '__main__':
    app.run(debug=True)