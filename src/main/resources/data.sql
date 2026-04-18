INSERT INTO articles (id, name, description, price, model) VALUES
('ART0000001', 'Laptop Gaming', 'Laptop de alta gama para gaming con GPU dedicada', 1299.99, 'XG-2024'),
('ART0000002', 'Monitor 4K', 'Monitor Ultra HD de 27 pulgadas', 499.99, 'MN-4K27'),
('ART0000003', 'Teclado Mec', 'Teclado mecánico RGB con switches Cherry MX', 149.99, 'KB-MX01')
ON CONFLICT (id) DO NOTHING;
