DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = current_schema()
          AND table_name = 'users'
    ) THEN
        IF NOT EXISTS (
            SELECT 1
            FROM pg_constraint
            WHERE conname = 'uk_user_email'
        ) THEN
            ALTER TABLE users
                ADD CONSTRAINT uk_user_email UNIQUE (email);
        END IF;

        IF NOT EXISTS (
            SELECT 1
            FROM pg_constraint
            WHERE conname = 'uk_user_student_no'
        ) THEN
            ALTER TABLE users
                ADD CONSTRAINT uk_user_student_no UNIQUE (student_no);
        END IF;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = current_schema()
          AND table_name = 'roommate'
    ) THEN
        IF NOT EXISTS (
            SELECT 1
            FROM pg_constraint
            WHERE conname = 'uk_roommate_user_no'
        ) THEN
            ALTER TABLE roommate
                ADD CONSTRAINT uk_roommate_user_no UNIQUE (user_no);
        END IF;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = current_schema()
          AND table_name = 'room_request'
    ) THEN
        IF NOT EXISTS (
            SELECT 1
            FROM pg_constraint
            WHERE conname = 'uk_room_request_user_room_direction'
        ) THEN
            ALTER TABLE room_request
                ADD CONSTRAINT uk_room_request_user_room_direction
                    UNIQUE (user_no, room_no, direction);
        END IF;
    END IF;
END $$;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = current_schema()
          AND table_name = 'chat_room'
    ) THEN
        ALTER TABLE chat_room
            DROP CONSTRAINT IF EXISTS uk_chat_room_direct;

        IF NOT EXISTS (
            SELECT 1
            FROM pg_indexes
            WHERE schemaname = current_schema()
              AND indexname = 'uk_chat_room_group'
        ) THEN
            EXECUTE 'CREATE UNIQUE INDEX uk_chat_room_group ON chat_room (room_no) WHERE chat_room_type = ''GROUP''';
        END IF;

        IF NOT EXISTS (
            SELECT 1
            FROM pg_indexes
            WHERE schemaname = current_schema()
              AND indexname = 'uk_chat_room_direct'
        ) THEN
            EXECUTE 'CREATE UNIQUE INDEX uk_chat_room_direct ON chat_room (room_no, applicant_user_no) WHERE chat_room_type = ''DIRECT''';
        END IF;
    END IF;
END $$;
