from flask import Flask, request, jsonify
import joblib

app = Flask(__name__)

model = joblib.load("model.pkl")

@app.route("/predict", methods=["POST"])
def predict():

    data = request.json

    features = [[
        data["ph"],
        data["tds"],
        data["turbidity"],
        data["temperature"]
    ]]

    prediction = model.predict(features)[0]

    probability = float(
        model.predict_proba(features)[0][1]
    )

    return jsonify({
        "outbreakRisk":
            "HIGH" if prediction == 1 else "LOW",
        "confidence":
            round(probability * 100, 2)
    })

if __name__ == "__main__":
    app.run(port=5000)
    