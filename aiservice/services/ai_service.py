def generate_description(text):
    return f"Generated Description: {text}"


def generate_recommendations(text):
    return [
        {
            "action_type": "Improve",
            "description": f"Improve clarity of: {text}",
            "priority": 1
        },
        {
            "action_type": "Translate",
            "description": "Translate text",
            "priority": 2
        },
        {
            "action_type": "Summarize",
            "description": "Summarize content",
            "priority": 3
        }
    ]