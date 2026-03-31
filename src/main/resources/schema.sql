CREATE UNIQUE INDEX IF NOT EXISTS uk_user_email
    ON users (email);

CREATE UNIQUE INDEX IF NOT EXISTS uk_user_student_no
    ON users (student_no);

CREATE UNIQUE INDEX IF NOT EXISTS uk_roommate_user_no
    ON roommate (user_no);

CREATE UNIQUE INDEX IF NOT EXISTS uk_room_like_user_room
    ON room_like (user_no, room_no);

CREATE UNIQUE INDEX IF NOT EXISTS uk_device_user_device
    ON devices (user_no, device_id);

CREATE UNIQUE INDEX IF NOT EXISTS uk_room_request_user_room_direction
    ON room_request (user_no, room_no, direction);

ALTER TABLE IF EXISTS chat_room
    DROP CONSTRAINT IF EXISTS uk_chat_room_direct;

CREATE UNIQUE INDEX IF NOT EXISTS uk_chat_room_group
    ON chat_room (room_no)
    WHERE chat_room_type = 'GROUP';

CREATE UNIQUE INDEX IF NOT EXISTS uk_chat_room_direct
    ON chat_room (room_no, applicant_user_no)
    WHERE chat_room_type = 'DIRECT';
