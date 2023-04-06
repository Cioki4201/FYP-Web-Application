from flask import Flask, request, jsonify
import RecommendationSystem

app = Flask(__name__)


@app.route('/recommend_games', methods=['POST'])
def recommend_games():
    user_games = request.json['user_games']
    recommended_games = RecommendationSystem.main(user_games)
    return jsonify(recommended_games)


if __name__ == '__main__':
    app.run(debug=True)