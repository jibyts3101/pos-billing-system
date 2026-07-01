CREATE DATABASE pos_billing_system;
USE pos_billing_system;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('SUPER_ADMIN','ADMIN','MANAGER','CASHIER') NOT NULL,
    phone VARCHAR(15),
    status ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE suppliers (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100),
    phone VARCHAR(15),
    email VARCHAR(100),
    address TEXT,
    gst_number VARCHAR(30),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    barcode VARCHAR(100) UNIQUE,
    product_name VARCHAR(150) NOT NULL,
    brand VARCHAR(100),

    category_id INT NOT NULL,
    supplier_id INT,

    purchase_price DECIMAL(10,2) NOT NULL,
    selling_price DECIMAL(10,2) NOT NULL,

    gst_percentage DECIMAL(5,2) DEFAULT 0,

    stock_quantity INT DEFAULT 0,
    minimum_stock INT DEFAULT 10,

    manufacture_date DATE,
    expiry_date DATE,

    product_image VARCHAR(255),

    status ENUM('AVAILABLE','OUT_OF_STOCK') DEFAULT 'AVAILABLE',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (category_id)
        REFERENCES categories(category_id),

    FOREIGN KEY (supplier_id)
        REFERENCES suppliers(supplier_id)
);

CREATE TABLE customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100),
    phone VARCHAR(15) UNIQUE,
    email VARCHAR(100),
    address TEXT,
    loyalty_points INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bills (
    bill_id INT AUTO_INCREMENT PRIMARY KEY,

    invoice_number VARCHAR(50) UNIQUE NOT NULL,

    customer_id INT,
    user_id INT NOT NULL,

    subtotal DECIMAL(10,2) NOT NULL,

    discount DECIMAL(10,2) DEFAULT 0,

    gst_amount DECIMAL(10,2) DEFAULT 0,

    total_amount DECIMAL(10,2) NOT NULL,

    payment_status ENUM('PAID','PENDING','PARTIAL')
        DEFAULT 'PAID',

    bill_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (customer_id)
        REFERENCES customers(customer_id),

    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);


CREATE TABLE bill_items (
    bill_item_id INT AUTO_INCREMENT PRIMARY KEY,

    bill_id INT NOT NULL,

    product_id INT NOT NULL,

    quantity INT NOT NULL,

    unit_price DECIMAL(10,2) NOT NULL,

    discount DECIMAL(10,2) DEFAULT 0,

    gst DECIMAL(10,2) DEFAULT 0,

    total DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (bill_id)
        REFERENCES bills(bill_id)
        ON DELETE CASCADE,

    FOREIGN KEY (product_id)
        REFERENCES products(product_id)
);

CREATE TABLE payments (

    payment_id INT AUTO_INCREMENT PRIMARY KEY,

    bill_id INT NOT NULL,

    payment_method ENUM(
        'CASH',
        'CARD',
        'UPI',
        'WALLET'
    ) NOT NULL,

    amount DECIMAL(10,2) NOT NULL,

    transaction_reference VARCHAR(100),

    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (bill_id)
        REFERENCES bills(bill_id)
);

CREATE TABLE inventory_logs (

    log_id INT AUTO_INCREMENT PRIMARY KEY,

    product_id INT NOT NULL,

    quantity_changed INT NOT NULL,

    action ENUM(
        'PURCHASE',
        'SALE',
        'RETURN',
        'ADJUSTMENT'
    ),

    remarks VARCHAR(255),

    log_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (product_id)
        REFERENCES products(product_id)
);

CREATE TABLE returns (

    return_id INT AUTO_INCREMENT PRIMARY KEY,

    bill_id INT NOT NULL,

    product_id INT NOT NULL,

    quantity INT NOT NULL,

    refund_amount DECIMAL(10,2),

    reason VARCHAR(255),

    return_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (bill_id)
        REFERENCES bills(bill_id),

    FOREIGN KEY (product_id)
        REFERENCES products(product_id)
);

CREATE TABLE coupons (

    coupon_id INT AUTO_INCREMENT PRIMARY KEY,

    coupon_code VARCHAR(30) UNIQUE NOT NULL,

    discount_percentage DECIMAL(5,2),

    minimum_purchase DECIMAL(10,2),

    expiry_date DATE,

    status ENUM(
        'ACTIVE',
        'EXPIRED'
    ) DEFAULT 'ACTIVE'
);

CREATE TABLE audit_logs (

    log_id INT AUTO_INCREMENT PRIMARY KEY,

    user_id INT,

    action VARCHAR(100),

    description TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);