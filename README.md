# SEO Tag Generator (Spring Boot)

This is a smart backend service that helps content creators and website owners boost their SEO rankings by generating highly relevant keywords (tags) from article titles. It leverages AI to suggest tags that help your content rank higher on search engines and attract more organic traffic
## 🔧 Features

- 🔍 AI-Powered Tag Generation
- 🤖 Use multiple OpenAI models with fallback to ensure availability
- ⚡ Fast responses using Redis caching
- 🧠 Secondary storage with MongoDB (fallback if Redis misses)
- 🔁 Non-blocking HTTP using Spring WebFlux and WebClient
- ✅ Input validation with Jakarta Validation
- ⚡Rate limiting per user to prevent abuse

## 🛠️ Tech Stack

- Java + Spring Boot
- Spring WebFlux for reactive endpoints
- WebClient for calling external AI APIs
- Lombok to reduce boilerplate code
- OpenRouter (AI API gateway)
- Redis (caching)
- MongoDB (persistent storage)
- Jakarta Validation


## 📄 API Endpoint

### `POST /api/generate-tags`

**Request Body:**
```json
{
  "title": "How to Improve Website Traffic with Content"
}
```
**Response**

```json
{
  "tags": [
    "SEO Tips",
    "Blogging",
    "Search Engine Optimization",
    "Content Strategy"
  ]
}
```

## 🧠 How It Works
1. User sends a `POST` request with the title of their article
2. The system first checks Redis for cached results.
3. If not found, it checks MongoDB.
4. If still not found, it queries the OpenRouter AI using fallback model logic.
5. The response is stored in MongoDB and Redis for future use.
6. Tags are returned instantly to the user.

## 🚀 Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/AbdelRahmanH1/SEOgenix.git
   cd Online-Exam-System--spring
   ```
2. **Configure your `.evn`**
   ```bash
    MONGO_URI=
    REDIS_URL=
    REDIS_PORT=
    OPEN_ROUTER_KEY=
   ```
3. Run the application
   ```bash
    ./mvnw spring-boot:run
   ```
---

## 🧑 Author

Made with 💻 & ☕ by [Abdelrahman Hossam](https://github.com/AbdelRahmanH1)

---

## 📌 License
This project is open-source and available under the [MIT License](LICENSE).

