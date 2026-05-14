SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE customers_bookings;
TRUNCATE TABLE bookings;
TRUNCATE TABLE trip_segments;
TRUNCATE TABLE travels;
TRUNCATE TABLE offers;
TRUNCATE TABLE hotels;
TRUNCATE TABLE buses;
TRUNCATE TABLE drivers;
TRUNCATE TABLE employees;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO employees (name, surname, gender, work_hour, hired, password)
VALUES
    ('Juan', 'García', 'MALE', 40, true, '123456'),
    ('Ana', 'López', 'FEMALE', 35, true, '123456'),
    ('Carlos', 'Martínez', 'MALE', 40, true, '123456');

INSERT INTO drivers (name, phone, licence_active, image_url) VALUES
    ('Pedro',  '612345678', true, null),
    ('Laura',  '698765432', true, null),
    ('Miguel', '634567890', true, null);

INSERT INTO buses (license_plate, capacity, bath, wifi, ac, usb) VALUES
    ('1234ABC', 50, true, true, true, true),
    ('5678DEF', 40, false, true, true, false),
    ('9012GHI', 60, true, false, true, true);

INSERT INTO hotels (name, address, city, country, stars, capacity, available_places, half_board_price, full_board_price, image_url, active) VALUES
    ('Hotel Madrid', 'Calle Gran Vía 1', 'Madrid', 'España', 4, 100, 100, 80.0, 120.0, null, true),
    ('Hotel Barcelona', 'Las Ramblas 25', 'Barcelona', 'España', 5, 150, 150, 100.0, 150.0, null, true),
    ('Hotel Sevilla', 'Avenida de la Constitución 5', 'Sevilla', 'España', 3, 80, 80, 60.0, 90.0, null, true);

INSERT INTO offers (discount_percentage, start_date, end_date) VALUES
    (10.0, '2026-05-01', '2026-08-31');

INSERT INTO users (name, surname, email, dni, age, active) VALUES
    ('María', 'García', 'maria@email.com', '12345678A', 30, true),
    ('Carlos', 'López', 'carlos@email.com', '87654321B', 25, true),
    ('Ana', 'Martínez', 'ana@email.com', '11223344C', 45, true),
    ('Luis', 'Pérez', 'luis@email.com', '44332211D', 10, true);

-- Assign tutor for Luis Pérez (minor, age 10) → María García as tutor
UPDATE users SET tutor_id = 1 WHERE id = 4;

INSERT INTO travels (destiny, start_date, end_date, sale, available_places, hotel_id, active) VALUES
    ('París', '2026-06-01', '2026-06-07', false, 50, 1, true),
    ('Roma', '2026-07-15', '2026-07-22', true, 40, 2, true),
    ('Londres', '2026-08-10', '2026-08-17', false, 60, 3, true);

-- Apply discount offer to Roma travel
UPDATE travels SET offer_id = 1 WHERE id = 2;

INSERT INTO trip_segments (travel_id, origin, destination, start_time, end_time, bus_id, driver_id) VALUES
    (1, 'Madrid', 'París',   '2026-06-01 08:00:00', '2026-06-01 20:00:00', 1, 1),
    (2, 'Madrid', 'Roma',    '2026-07-15 07:00:00', '2026-07-15 19:00:00', 2, 2),
    (3, 'Madrid', 'Londres', '2026-08-10 09:00:00', '2026-08-10 21:00:00', 3, 3);

INSERT INTO bookings (bought_date, type_board, is_group, total_price, travels_id, employee_id) VALUES
    ('2026-04-15 10:30:00', 'HALF', false, 800.0,  1, 1),
    ('2026-04-20 14:00:00', 'FULL', true,  1500.0, 2, 2);

INSERT INTO customers_bookings (booking_id, customer_id) VALUES
    (1, 1),
    (1, 2),
    (2, 3);