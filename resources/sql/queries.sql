-- name: directors
-- Return all directors.
SELECT *
FROM DIRECTORS

-- name: count-directors
-- Count all the directors
SELECT count(*) AS count
FROM DIRECTORS

-- name: save-director<!
-- Insert a director
INSERT INTO DIRECTORS ( name ) VALUES ( :name )

-- name: create-table!
-- create the directors table if it does not exist
CREATE TABLE IF NOT EXISTS DIRECTORS(ID IDENTITY PRIMARY KEY, NAME VARCHAR(255))

-- name: delete-director!
-- Deletes a director
DELETE FROM DIRECTORS WHERE NAME=:name
