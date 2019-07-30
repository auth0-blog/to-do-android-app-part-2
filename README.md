# Android Tutorial: Building and Securing Your First App (Part 2)

In the second part of this tutorial, you will learn how to integrate your Android application with a backend API. For starters, you will spin up a simple REST API (you will have different alternatives to achieve that), then you will adjust your app to replace the static to-do list with one provided by this API. In the end, you will leverage the app integration with Auth0 to make it use a private (also referred to as secured) endpoint to persist new to-do items.

Read more at: https://auth0.com/blog/android-tutorial-building-and-securing-your-first-app-part-2/

## Updating ULP Texts

```bash
curl -X PUT \
  https://digio-sample.auth0.com/api/v2/prompts/login/custom-texts \
  -H 'Authorization: Bearer eyJ...iKw' \
  -H 'Content-Type: application/json' \
  -d '{
    "login": {
        "usernamePlaceholder": {
            "pt-BR": "Ex. 123.456.789-00"
        },
        "description": {
            "pt-BR": "Para continuar, insira seu CPF e sua senha."
        }
    }
}'
```