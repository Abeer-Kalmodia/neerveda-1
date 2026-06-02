import pandas as pd
from sklearn.ensemble import RandomForestClassifier
import joblib

data = pd.DataFrame({
    "ph":[5.8,6.2,6.8,7.0,7.5,8.7,5.5,7.1],
    "tds":[620,550,300,150,180,650,700,220],
    "turbidity":[8.5,6.0,3.0,1.0,2.0,7.0,9.0,2.0],
    "temperature":[28,30,24,22,25,35,33,23],
    "outbreak":[1,1,0,0,0,1,1,0]
})

X = data[["ph","tds","turbidity","temperature"]]
y = data["outbreak"]

model = RandomForestClassifier(n_estimators=50)
model.fit(X,y)

joblib.dump(model,"model.pkl")

print("Model trained successfully")