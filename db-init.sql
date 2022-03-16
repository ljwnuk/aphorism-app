DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Aphorisms CASCADE;
DROP TABLE IF EXISTS Ratings CASCADE;

CREATE TABLE Users (
    login TEXT PRIMARY KEY,
    password TEXT NOT NULL   -- Password hashing to be implemented.
);

CREATE TABLE Aphorisms (
    id SERIAL PRIMARY KEY,
    user_login TEXT NOT NULL REFERENCES Users,
    content TEXT NOT NULL
);

CREATE TABLE Ratings (
    aphorism_id INTEGER NOT NULL REFERENCES Aphorisms,
    user_login TEXT NOT NULL REFERENCES Users,
    value INTEGER NOT NULL,
    comment TEXT NOT NULL
);

INSERT INTO Users VALUES
    ('Alexios', 'abcdef'),
    ('Kassandra', 'xyz1234'),
    ('Markos', 'dummy');

INSERT INTO Aphorisms (user_login, content) VALUES
    ('Alexios', 'Look at the clouds and be happy!'),
    ('Alexios', 'Just sit and contemplate.'),
    ('Kassandra', 'We''re all just dust.'),
    ('Kassandra', 'Are you making a good use of your time?'),
    ('Kassandra', 'Think about all the necessary things.');

INSERT INTO Ratings VALUES
    (1, 'Markos', 3, 'Totally stupid!'),
    (1, 'Kassandra', 5, 'Somewhat irrelevant...'),
    (2, 'Kassandra', 7, 'I think I get a sense of it.'),
    (4, 'Markos', 1, 'Rubbish.'),
    (4, 'Markos', 2, 'I just don''t like that kind of thoughts'),
    (4, 'Alexios', 1, 'What?');
