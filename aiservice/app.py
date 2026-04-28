from flask import Flask
from routes.ai_routes import ai_bp   # import your routes

app = Flask(__name__)

# ✅ Register Blueprint (VERY IMPORTANT)
app.register_blueprint(ai_bp)

# ✅ Optional: Home route (for testing server)
@app.route("/")
def home():
    return {"message": "AI Service is running"}

# ✅ Debug: Print all routes (helps avoid 404 issues)
print("Available routes:")
print(app.url_map)

if __name__ == "__main__":
    app.run(host="127.0.0.1", port=5000, debug=True)