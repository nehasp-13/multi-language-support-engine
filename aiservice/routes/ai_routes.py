from flask import Blueprint, request, jsonify
from datetime import datetime

# ✅ Blueprint with prefix
ai_bp = Blueprint("ai", __name__, url_prefix="/ai")


# =========================
# DAY 3 - DESCRIBE API
# =========================
@ai_bp.route("/describe", methods=["POST"])
def describe():
    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "text required"}), 400

    text = data["text"]

    return jsonify({
        "generated_at": "now",
        "result": f"Generated Description: {text}"
    })


# =========================
# DAY 4 - RECOMMEND API
# =========================
@ai_bp.route("/recommend", methods=["POST"])
def recommend():
    data = request.get_json()

    if not data or "text" not in data:
        return jsonify({"error": "text required"}), 400

    text = data["text"]

    recommendations = [
        {
            "action_type": "Improve",
            "description": f"Improve clarity of: {text}",
            "priority": 1
        },
        {
            "action_type": "Expand",
            "description": f"Add more details to: {text}",
            "priority": 2
        },
        {
            "action_type": "Summarize",
            "description": f"Summarize the content: {text}",
            "priority": 3
        }
    ]

    return jsonify({
        "generated_at": datetime.now().isoformat(),
        "recommendations": recommendations
    })