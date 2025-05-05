package com.example.everestclothing.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.everestclothing.models.Product;
import com.example.everestclothing.models.User;
import com.example.everestclothing.models.Order;
import com.example.everestclothing.models.CartItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "everest_clothing.db";
    private static final int DATABASE_VERSION = 2;

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_ORDER_ITEMS = "order_items";
    private static final String TABLE_CART = "cart";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // Users Table Columns
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PHONE = "phone";

    // Products Table Columns
    private static final String KEY_PRODUCT_NAME = "name";
    private static final String KEY_PRODUCT_DESCRIPTION = "description";
    private static final String KEY_PRODUCT_PRICE = "price";
    private static final String KEY_PRODUCT_IMAGE = "image";
    private static final String KEY_PRODUCT_CATEGORY = "category";
    private static final String KEY_PRODUCT_SIZES = "available_sizes";

    // Orders Table Columns
    private static final String KEY_ORDER_USER_ID = "user_id";
    private static final String KEY_ORDER_TOTAL = "total";
    private static final String KEY_ORDER_STATUS = "status";
    private static final String KEY_ORDER_SHIPPING_ADDRESS = "shipping_address";

    // Order Items Table Columns
    private static final String KEY_ORDER_ITEM_ORDER_ID = "order_id";
    private static final String KEY_ORDER_ITEM_PRODUCT_ID = "product_id";
    private static final String KEY_ORDER_ITEM_QUANTITY = "quantity";
    private static final String KEY_ORDER_ITEM_PRICE = "price";
    private static final String KEY_ORDER_ITEM_SIZE = "size";

    // Cart Table Columns
    private static final String KEY_CART_USER_ID = "user_id";
    private static final String KEY_CART_PRODUCT_ID = "product_id";
    private static final String KEY_CART_QUANTITY = "quantity";
    private static final String KEY_CART_SIZE = "size";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERNAME + " TEXT UNIQUE,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_FULL_NAME + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create Products table
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_PRODUCT_NAME + " TEXT,"
                + KEY_PRODUCT_DESCRIPTION + " TEXT,"
                + KEY_PRODUCT_PRICE + " REAL,"
                + KEY_PRODUCT_IMAGE + " TEXT,"
                + KEY_PRODUCT_CATEGORY + " TEXT,"
                + KEY_PRODUCT_SIZES + " TEXT DEFAULT 'S,M,L,XL',"
                + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        // Create Orders table
        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ORDER_USER_ID + " INTEGER,"
                + KEY_ORDER_TOTAL + " REAL,"
                + KEY_ORDER_STATUS + " TEXT,"
                + KEY_ORDER_SHIPPING_ADDRESS + " TEXT,"
                + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(" + KEY_ORDER_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + ")"
                + ")";
        db.execSQL(CREATE_ORDERS_TABLE);

        // Create Order Items table
        String CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE " + TABLE_ORDER_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ORDER_ITEM_ORDER_ID + " INTEGER,"
                + KEY_ORDER_ITEM_PRODUCT_ID + " INTEGER,"
                + KEY_ORDER_ITEM_QUANTITY + " INTEGER,"
                + KEY_ORDER_ITEM_PRICE + " REAL,"
                + KEY_ORDER_ITEM_SIZE + " TEXT DEFAULT 'M',"
                + "FOREIGN KEY(" + KEY_ORDER_ITEM_ORDER_ID + ") REFERENCES " + TABLE_ORDERS + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_ORDER_ITEM_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + KEY_ID + ")"
                + ")";
        db.execSQL(CREATE_ORDER_ITEMS_TABLE);

        // Create Cart table
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CART_USER_ID + " INTEGER,"
                + KEY_CART_PRODUCT_ID + " INTEGER,"
                + KEY_CART_QUANTITY + " INTEGER,"
                + KEY_CART_SIZE + " TEXT DEFAULT 'M',"
                + "FOREIGN KEY(" + KEY_CART_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_CART_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + KEY_ID + ")"
                + ")";
        db.execSQL(CREATE_CART_TABLE);

        // Insert some sample products
        insertSampleProducts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add size column to cart table if upgrading from version 1
            db.execSQL("ALTER TABLE " + TABLE_CART + " ADD COLUMN " + KEY_CART_SIZE + " TEXT DEFAULT 'M'");
            db.execSQL("ALTER TABLE " + TABLE_ORDER_ITEMS + " ADD COLUMN " + KEY_ORDER_ITEM_SIZE + " TEXT DEFAULT 'M'");
            db.execSQL("ALTER TABLE " + TABLE_PRODUCTS + " ADD COLUMN " + KEY_PRODUCT_SIZES + " TEXT DEFAULT 'S,M,L,XL'");
        } else {
            // Drop older tables if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

            // Create tables again
            onCreate(db);
        }
    }

    private void insertSampleProducts(SQLiteDatabase db) {
        String[] productNames = {
                "Urban Puffer Jacket", "Distressed Cargo Jeans", "Graphic Streetwear Tee", "Oversized Button-Up",
                "Utility Jumpsuit", "High-Top Sneakers", "Combat Boots", "Vintage Washed Hoodie",
                "Statement Neck Scarf", "Skater Shorts", "Oversized Hoodie", "Tactical Cargo Pants",
                "Urban Art Tee", "Color Block Jacket", "Graffiti Print Sweatshirt", "Baggy Ripped Jeans",
                "Retro Puffer Vest", "Beanie with Patch", "Chunky Platform Sneakers", "Relaxed Skate Shorts"
        };
        
        String[] productDescriptions = {
                "Urban style puffer jacket with oversized silhouette and hidden pockets.",
                "Distressed cargo jeans with multiple pockets and relaxed fit.",
                "Bold graphic tee featuring street art designs and dropped shoulders.",
                "Oversized button-up shirt with raw hem and minimal detailing.",
                "Utilitarian jumpsuit with adjustable straps and multiple pockets.",
                "Classic high-top sneakers with chunky sole and street-ready design.",
                "Heavy-duty combat boots with platform sole and reinforced toe.",
                "Vintage-washed oversized hoodie with minimalist design.",
                "Bold statement neck scarf with unique street art print.",
                "Loose-fit skater shorts perfect for urban movement.",
                "Oversized unisex hoodie with minimalist design, perfect for street style.",
                "Tactical cargo pants with multiple pockets for urban exploration.",
                "Bold graphic tee featuring abstract street art designs.",
                "Streetwear-inspired jacket with trendy color blocking and urban aesthetics.",
                "Comfortable unisex sweatshirt with urban graffiti prints.",
                "Relaxed fit baggy jeans with distressed details for authentic street style.",
                "Trendy unisex puffer vest that layers with any streetwear outfit.",
                "Bold statement beanie with unique embroidered patches.",
                "Premium chunky platform sneakers designed for street style.",
                "Loose-fitting skater shorts, perfect for active urban lifestyles."
        };
        
        double[] productPrices = {
                99.99, 59.99, 19.99, 45.99, 
                69.99, 79.99, 89.99, 49.99, 
                29.99, 24.99, 64.99, 54.99,
                29.99, 79.99, 49.99, 69.99,
                59.99, 24.99, 89.99, 39.99
        };
        
        String[] productImages = {
                "winter_jacket", "denim_jeans", "cotton_tshirt", "formal_shirt",
                "summer_dress", "sports_shoes", "leather_boots", "woolen_sweater",
                "silk_scarf", "casual_shorts", "oversized_hoodie", "cargo_pants",
                "graphic_tee", "streetwear_jacket", "urban_sweatshirt", "baggy_jeans",
                "puffer_vest", "statement_beanie", "urban_sneakers", "skater_shorts"
        };
        
        String[] productCategories = {
                "Streetwear", "Streetwear", "Streetwear", "Streetwear",
                "Streetwear", "Footwear", "Footwear", "Streetwear",
                "Accessories", "Streetwear", "Streetwear", "Streetwear",
                "Streetwear", "Streetwear", "Streetwear", "Streetwear",
                "Streetwear", "Accessories", "Footwear", "Streetwear"
        };
        
        // Define available sizes for each product
        String[] productSizes = {
                "XS,S,M,L,XL,XXL", // Urban Puffer Jacket
                "28,30,32,34,36,38", // Distressed Cargo Jeans
                "XS,S,M,L,XL,XXL", // Graphic Streetwear Tee
                "S,M,L,XL", // Oversized Button-Up
                "XS,S,M,L", // Utility Jumpsuit
                "7,8,9,10,11,12", // High-Top Sneakers
                "7,8,9,10,11,12", // Combat Boots
                "S,M,L,XL", // Vintage Washed Hoodie
                "One Size", // Statement Neck Scarf
                "S,M,L,XL", // Skater Shorts
                "S,M,L,XL,XXL", // Oversized Hoodie
                "S,M,L,XL", // Tactical Cargo Pants
                "S,M,L,XL,XXL", // Urban Art Tee
                "S,M,L,XL", // Color Block Jacket
                "XS,S,M,L,XL,XXL", // Graffiti Print Sweatshirt
                "28,30,32,34,36,38", // Baggy Ripped Jeans
                "S,M,L,XL", // Retro Puffer Vest
                "One Size", // Beanie with Patch
                "7,8,9,10,11,12", // Chunky Platform Sneakers
                "S,M,L,XL" // Relaxed Skate Shorts
        };
        
        for (int i = 0; i < productNames.length; i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_PRODUCT_NAME, productNames[i]);
            values.put(KEY_PRODUCT_DESCRIPTION, productDescriptions[i]);
            values.put(KEY_PRODUCT_PRICE, productPrices[i]);
            values.put(KEY_PRODUCT_IMAGE, productImages[i]);
            values.put(KEY_PRODUCT_CATEGORY, productCategories[i]);
            values.put(KEY_PRODUCT_SIZES, productSizes[i]);
            
            db.insert(TABLE_PRODUCTS, null, values);
        }
    }

    // User CRUD Operations
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_FULL_NAME, user.getFullName());
        values.put(KEY_ADDRESS, user.getAddress());
        values.put(KEY_PHONE, user.getPhone());
        
        // Insert row
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }
    
    public User getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_USERS, 
                new String[] { KEY_ID, KEY_USERNAME, KEY_EMAIL, KEY_PASSWORD, 
                        KEY_FULL_NAME, KEY_ADDRESS, KEY_PHONE },
                KEY_ID + "=?", new String[] { String.valueOf(id) }, 
                null, null, null, null);
        
        if (cursor != null)
            cursor.moveToFirst();
        
        User user = new User(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6)
        );
        
        cursor.close();
        return user;
    }
    
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_USERS, 
                new String[] { KEY_ID, KEY_USERNAME, KEY_EMAIL, KEY_PASSWORD, 
                        KEY_FULL_NAME, KEY_ADDRESS, KEY_PHONE },
                KEY_EMAIL + "=?", new String[] { email }, 
                null, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)
            );
            cursor.close();
            return user;
        }
        
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_USERS, 
                new String[] { KEY_ID, KEY_USERNAME, KEY_EMAIL, KEY_PASSWORD, 
                        KEY_FULL_NAME, KEY_ADDRESS, KEY_PHONE },
                KEY_USERNAME + "=?", new String[] { username }, 
                null, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)
            );
            cursor.close();
            return user;
        }
        
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }
    
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_FULL_NAME, user.getFullName());
        values.put(KEY_ADDRESS, user.getAddress());
        values.put(KEY_PHONE, user.getPhone());
        
        // Update row
        return db.update(TABLE_USERS, values, KEY_ID + "=?", 
                new String[] { String.valueOf(user.getId()) });
    }
    
    // Product CRUD Operations
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                
                productList.add(product);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return productList;
    }
    
    public Product getProduct(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_PRODUCTS, 
                new String[] { KEY_ID, KEY_PRODUCT_NAME, KEY_PRODUCT_DESCRIPTION, 
                        KEY_PRODUCT_PRICE, KEY_PRODUCT_IMAGE, KEY_PRODUCT_CATEGORY, KEY_PRODUCT_SIZES },
                KEY_ID + "=?", new String[] { String.valueOf(id) }, 
                null, null, null, null);
        
        if (cursor != null)
            cursor.moveToFirst();
        
        Product product = new Product(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getDouble(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6)
        );
        
        cursor.close();
        return product;
    }

    public List<Product> searchProducts(String query) {
        List<Product> productList = new ArrayList<>();
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, 
                new String[] { KEY_ID, KEY_PRODUCT_NAME, KEY_PRODUCT_DESCRIPTION, 
                        KEY_PRODUCT_PRICE, KEY_PRODUCT_IMAGE, KEY_PRODUCT_CATEGORY, KEY_PRODUCT_SIZES },
                KEY_PRODUCT_NAME + " LIKE ? OR " + KEY_PRODUCT_DESCRIPTION + " LIKE ? OR " + 
                KEY_PRODUCT_CATEGORY + " LIKE ?", 
                new String[] { "%" + query + "%", "%" + query + "%", "%" + query + "%" }, 
                null, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                
                productList.add(product);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return productList;
    }
    
    public List<Product> getProductsByCategory(String category) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor;
        if (category == null || category.equals("All")) {
            cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, null);
        } else {
            cursor = db.query(
                TABLE_PRODUCTS,
                null,
                KEY_PRODUCT_CATEGORY + "=?",
                new String[]{category},
                null, null, null
            );
        }
        
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)
                );
                
                productList.add(product);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return productList;
    }
    
    // Cart operations
    public long addToCart(long userId, long productId, int quantity, String size) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Check if item already exists in cart with the same size
        Cursor cursor = db.query(TABLE_CART, 
                new String[] { KEY_ID, KEY_CART_QUANTITY },
                KEY_CART_USER_ID + "=? AND " + KEY_CART_PRODUCT_ID + "=? AND " + KEY_CART_SIZE + "=?", 
                new String[] { String.valueOf(userId), String.valueOf(productId), size }, 
                null, null, null, null);
        
        long id;
        
        if (cursor != null && cursor.moveToFirst()) {
            // Update existing item
            int existingQuantity = cursor.getInt(1);
            int newQuantity = existingQuantity + quantity;
            
            ContentValues values = new ContentValues();
            values.put(KEY_CART_QUANTITY, newQuantity);
            
            id = db.update(TABLE_CART, values, 
                    KEY_CART_USER_ID + "=? AND " + KEY_CART_PRODUCT_ID + "=? AND " + KEY_CART_SIZE + "=?", 
                    new String[] { String.valueOf(userId), String.valueOf(productId), size });
        } else {
            // Add new item
            ContentValues values = new ContentValues();
            values.put(KEY_CART_USER_ID, userId);
            values.put(KEY_CART_PRODUCT_ID, productId);
            values.put(KEY_CART_QUANTITY, quantity);
            values.put(KEY_CART_SIZE, size);
            
            id = db.insert(TABLE_CART, null, values);
        }
        
        if (cursor != null) {
            cursor.close();
        }
        
        return id;
    }
    
    // Overloaded method for backward compatibility
    public long addToCart(long userId, long productId, int quantity) {
        return addToCart(userId, productId, quantity, "M"); // Default to Medium size
    }
    
    public List<CartItem> getCartItems(long userId) {
        List<CartItem> cartItems = new ArrayList<>();
        
        String selectQuery = "SELECT c." + KEY_ID + ", c." + KEY_CART_PRODUCT_ID + 
                ", c." + KEY_CART_QUANTITY + ", c." + KEY_CART_SIZE + ", p." + KEY_PRODUCT_NAME + 
                ", p." + KEY_PRODUCT_PRICE + ", p." + KEY_PRODUCT_IMAGE +
                " FROM " + TABLE_CART + " c" +
                " INNER JOIN " + TABLE_PRODUCTS + " p ON c." + KEY_CART_PRODUCT_ID + 
                " = p." + KEY_ID +
                " WHERE c." + KEY_CART_USER_ID + " = " + userId;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                CartItem item = new CartItem(
                        cursor.getLong(0),
                        userId,
                        cursor.getLong(1),
                        cursor.getInt(2),
                        cursor.getString(4),
                        cursor.getDouble(5),
                        cursor.getString(6),
                        cursor.getString(3)
                );
                
                cartItems.add(item);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return cartItems;
    }
    
    public int updateCartItemQuantity(long cartId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_CART_QUANTITY, quantity);
        
        // Update row
        return db.update(TABLE_CART, values, KEY_ID + "=?", 
                new String[] { String.valueOf(cartId) });
    }
    
    public void deleteCartItem(long cartId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, KEY_ID + "=?", 
                new String[] { String.valueOf(cartId) });
    }
    
    public void clearCart(long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, KEY_CART_USER_ID + "=?", 
                new String[] { String.valueOf(userId) });
    }
    
    // Order operations
    public long createOrder(Order order, List<CartItem> cartItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        
        try {
            // Insert order
            ContentValues orderValues = new ContentValues();
            orderValues.put(KEY_ORDER_USER_ID, order.getUserId());
            orderValues.put(KEY_ORDER_TOTAL, order.getTotal());
            orderValues.put(KEY_ORDER_STATUS, order.getStatus());
            orderValues.put(KEY_ORDER_SHIPPING_ADDRESS, order.getShippingAddress());
            
            long orderId = db.insert(TABLE_ORDERS, null, orderValues);
            
            // Insert order items
            for (CartItem item : cartItems) {
                ContentValues itemValues = new ContentValues();
                itemValues.put(KEY_ORDER_ITEM_ORDER_ID, orderId);
                itemValues.put(KEY_ORDER_ITEM_PRODUCT_ID, item.getProductId());
                itemValues.put(KEY_ORDER_ITEM_QUANTITY, item.getQuantity());
                itemValues.put(KEY_ORDER_ITEM_PRICE, item.getPrice());
                itemValues.put(KEY_ORDER_ITEM_SIZE, item.getSize());
                
                db.insert(TABLE_ORDER_ITEMS, null, itemValues);
            }
            
            // Clear user's cart
            clearCart(order.getUserId());
            
            db.setTransactionSuccessful();
            return orderId;
        } finally {
            db.endTransaction();
        }
    }
    
    public List<Order> getUserOrders(long userId) {
        List<Order> orders = new ArrayList<>();
        
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + 
                " WHERE " + KEY_ORDER_USER_ID + " = " + userId +
                " ORDER BY " + KEY_CREATED_AT + " DESC";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getLong(0),
                        cursor.getLong(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                
                orders.add(order);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return orders;
    }
    
    public Order getOrderById(long orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Order order = null;
        
        Cursor cursor = db.query(TABLE_ORDERS, 
                null,
                KEY_ID + "=?", 
                new String[] { String.valueOf(orderId) }, 
                null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            order = new Order(
                    cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            cursor.close();
        }
        
        return order;
    }
    
    public List<CartItem> getOrderItems(long orderId) {
        List<CartItem> orderItems = new ArrayList<>();
        
        String selectQuery = "SELECT oi." + KEY_ID + ", oi." + KEY_ORDER_ITEM_PRODUCT_ID + 
                ", oi." + KEY_ORDER_ITEM_QUANTITY + ", oi." + KEY_ORDER_ITEM_SIZE + 
                ", p." + KEY_PRODUCT_NAME + ", oi." + KEY_ORDER_ITEM_PRICE + 
                ", p." + KEY_PRODUCT_IMAGE +
                " FROM " + TABLE_ORDER_ITEMS + " oi" +
                " INNER JOIN " + TABLE_PRODUCTS + " p ON oi." + KEY_ORDER_ITEM_PRODUCT_ID + 
                " = p." + KEY_ID +
                " WHERE oi." + KEY_ORDER_ITEM_ORDER_ID + " = " + orderId;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                CartItem item = new CartItem(
                        cursor.getLong(0),        // id
                        orderId,                  // using orderId instead of userId
                        cursor.getLong(1),        // productId
                        cursor.getInt(2),         // quantity
                        cursor.getString(4),      // name
                        cursor.getDouble(5),      // price
                        cursor.getString(6),      // imageUrl
                        cursor.getString(3)       // size
                );
                
                orderItems.add(item);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return orderItems;
    }
} 