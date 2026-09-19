DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'users' AND column_name = 'updates_at'
    ) THEN
        ALTER TABLE users RENAME COLUMN updates_at TO updated_at;
    END IF;
END $$;