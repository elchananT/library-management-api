CREATE TABLE loans (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGSERIAL NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    book_id BIGSERIAL NOT NULL REFERENCES books(id) ON DELETE CASCADE,
    borrowed_at TIMESTAMP NOT NULL DEFAULT now(),
    returned_at TIMESTAMP NULL
);

CREATE INDEX ix_loans_book_is ON loans(book_id);
CREATE INDEX ix_loans_user_is ON loans(user_id);
CREATE UNIQUE INDEX ux_loans_active_book ON loans(book_id) WHERE returned_at IS NULL;