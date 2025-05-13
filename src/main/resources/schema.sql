
CREATE TABLE IF NOT EXISTS users (
                                     user_idx INTEGER PRIMARY KEY AUTOINCREMENT,
                                     user_id TEXT NOT NULL UNIQUE,
                                     password TEXT,
                                     name TEXT,
                                     email TEXT
);

CREATE TABLE IF NOT EXISTS todos (
                                     todos_idx INTEGER PRIMARY KEY AUTOINCREMENT,
                                     user_idx INTEGER,
                                     title TEXT NOT NULL,
                                     description TEXT,
                                     completed BOOLEAN DEFAULT 0,
                                     FOREIGN KEY(user_idx) REFERENCES users(user_idx)
);