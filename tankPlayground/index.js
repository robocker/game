const express = require("express");

const app = express();

const PORT = 3000;

app.use(express.json())

app.get("/", (req, resp) => {
  resp.status(200);
  resp.send("Hello from tank playground!");
});

app.post("/tank/move", (req, resp) => {
  console.log(req.body);

  resp.status(200);
  resp.json({ resp: "is cool" });
});

app.listen(PORT, () => {
  console.log("It works");
});
