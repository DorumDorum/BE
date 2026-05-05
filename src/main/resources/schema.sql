CREATE UNIQUE INDEX IF NOT EXISTS uk_user_email
    ON users (email);

CREATE UNIQUE INDEX IF NOT EXISTS uk_user_student_no
    ON users (student_no);

ALTER TABLE IF EXISTS room_rule ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;

ALTER TABLE IF EXISTS roommate DROP CONSTRAINT IF EXISTS uk_roommate_user_no;
CREATE UNIQUE INDEX IF NOT EXISTS uk_roommate_user_no
    ON roommate (user_no)
    WHERE deleted_at IS NULL;

ALTER TABLE IF EXISTS room_like DROP CONSTRAINT IF EXISTS uk_room_like_user_room;
CREATE UNIQUE INDEX IF NOT EXISTS uk_room_like_user_room
    ON room_like (user_no, room_no)
    WHERE deleted_at IS NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uk_device_user_device
    ON devices (user_no, device_id);

ALTER TABLE IF EXISTS room_request DROP CONSTRAINT IF EXISTS uk_room_request_user_room_direction;
CREATE UNIQUE INDEX IF NOT EXISTS uk_room_request_user_room_direction
    ON room_request (user_no, room_no, direction)
    WHERE deleted_at IS NULL;

ALTER TABLE IF EXISTS chat_room_member DROP CONSTRAINT IF EXISTS uk_chat_room_member_room_user;
CREATE UNIQUE INDEX IF NOT EXISTS uk_chat_room_member_room_user
    ON chat_room_member (chat_room_no, user_no)
    WHERE deleted_at IS NULL;

ALTER TABLE IF EXISTS users
    ALTER COLUMN gender SET NOT NULL;

DROP INDEX IF EXISTS idx_room_status_created;
DROP INDEX IF EXISTS idx_room_status_remaining_created;

CREATE INDEX IF NOT EXISTS idx_room_status_gender_created
    ON room (room_status, gender, created_at DESC, room_no DESC);
CREATE INDEX IF NOT EXISTS idx_room_status_gender_remaining_created
    ON room (room_status, gender, remaining ASC, created_at DESC, room_no DESC);

ALTER TABLE IF EXISTS chat_room
    DROP CONSTRAINT IF EXISTS uk_chat_room_direct;

DROP INDEX IF EXISTS uk_chat_room_group;
CREATE UNIQUE INDEX IF NOT EXISTS uk_chat_room_group
    ON chat_room (room_no)
    WHERE chat_room_type = 'GROUP' AND deleted_at IS NULL;

DROP INDEX IF EXISTS uk_chat_room_direct;
CREATE UNIQUE INDEX IF NOT EXISTS uk_chat_room_direct
    ON chat_room (room_no, applicant_user_no)
    WHERE chat_room_type = 'DIRECT' AND deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_chat_room_room_no
    ON chat_room (room_no);
