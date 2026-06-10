const express = require("express");
const cors = require("cors");
const exercises = require("./exercises.json");

const app = express();
app.use(cors());

const BASE_IMAGE_URL =
  "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/";

app.get("/exercises", (req, res) => {
  const page = parseInt(req.query.page) || 1;
  const limit = parseInt(req.query.limit) || 20;

  const start = (page - 1) * limit;
  const end = start + limit;

  const data = exercises.slice(start, end).map(e => ({
    ...e,
    images: e.images?.map(img => BASE_IMAGE_URL + img)
  }));

  res.json({
    total: exercises.length,
    page,
    limit,
    data
  });
});

app.listen(3000, () => {
  console.log("API de ejercicios en el puerto http://localhost:3000");
});
