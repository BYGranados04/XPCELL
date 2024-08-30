from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_marshmallow import Marshmallow
from flask_cors import CORS

app = Flask(__name__)
CORS(app)
# Configuración para usar SQLite en lugar de MySQL
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///C:\\Users\\brenn\\OneDrive\\Escritorio\\Mis cosas\\1. UMG\\6to Semestre\\BD 1\\Proyecto\\XPCELL\\XPCELLDB.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)
ma = Marshmallow(app)

# Clases
class Usuario(db.Model):
    __tablename__ = 'usuarios'  # Tabla usuarios

    id_usuario = db.Column(db.Integer, primary_key=True)
    usuario = db.Column(db.String(100), unique=True, nullable=False)
    contrasena = db.Column(db.String(100), nullable=False)
    tipo_usuario = db.Column(db.String(50))

class Marca(db.Model):
    __tablename__ = 'marcas' # Tabla marcas
    id_marca = db.Column(db.Integer, primary_key=True)
    nombre_marca = db.Column(db.String(100), nullable=False)

    def __init__(self, nombre_marca):
        self.nombre_marca = nombre_marca

class TipoRepuesto(db.Model):
    __tablename__ = 'tiposRepuestos'
    id_tipo_repuesto = db.Column(db.Integer, primary_key=True, autoincrement=True)
    nombre_tipo_repuesto = db.Column(db.String(100), nullable=False)
    repuestos = db.relationship('Repuesto', backref='tipo_repuesto', lazy=True)

    def __init__(self, nombre_tipo_repuesto):
        self.nombre_tipo_repuesto = nombre_tipo_repuesto

class Repuesto(db.Model):
    __tablename__ = 'repuestos'
    id_repuesto = db.Column(db.Integer, primary_key=True, autoincrement=True)
    nombre_repuesto = db.Column(db.String(100), nullable=False)
    id_tipo_repuesto = db.Column(db.Integer, db.ForeignKey('tiposRepuestos.id_tipo_repuesto'), nullable=False)
    id_modelo = db.Column(db.Integer, db.ForeignKey('modelos.id_modelo'), nullable=False)
    precio = db.Column(db.Float, nullable=False)
    stock = db.Column(db.Integer, nullable=False)

class Modelo(db.Model):
    __tablename__ = 'modelos'
    id_modelo = db.Column(db.Integer, primary_key=True, autoincrement=True)
    nombre_modelo = db.Column(db.String(100), nullable=False)
    id_marca = db.Column(db.Integer, db.ForeignKey('marcas.id_marca'), nullable=False)
    repuestos = db.relationship('Repuesto', backref='modelo', lazy=True)

    def __init__(self, nombre_modelo, id_marca):
        self.nombre_modelo = nombre_modelo
        self.id_marca = id_marca

class Carrito(db.Model):
    __tablename__ = 'Carrito'
    id_carrito = db.Column(db.Integer, primary_key=True, autoincrement=True)
    id_usuario = db.Column(db.Integer, db.ForeignKey('usuarios.id_usuario'), nullable=False)
    usuario = db.relationship('Usuario', backref='carritos')

class DetalleCarrito(db.Model):
    __tablename__ = 'detalleCarrito'
    id_detalle = db.Column(db.Integer, primary_key=True, autoincrement=True)
    id_carrito = db.Column(db.Integer, db.ForeignKey('Carrito.id_carrito'), nullable=False)
    id_repuesto = db.Column(db.Integer, db.ForeignKey('repuestos.id_repuesto'), nullable=False)
    cantidad = db.Column(db.Integer, nullable=False)

    carrito = db.relationship('Carrito', backref='detalles')
    repuesto = db.relationship('Repuesto', backref='detalles')

class Orden(db.Model):
    __tablename__ = 'ordenes'
    id_orden = db.Column(db.Integer, primary_key=True, autoincrement=True)
    id_usuario = db.Column(db.Integer, db.ForeignKey('usuarios.id_usuario'), nullable=False)
    fecha_orden = db.Column(db.String, default=db.func.strftime('%d/%m/%Y', db.func.current_date()), nullable=False)
    total = db.Column(db.Float, nullable=False)

    def __init__(self, id_usuario, total):
        self.id_usuario = id_usuario
        self.total = total

class DetalleOrden(db.Model):
    __tablename__ = 'detalleOrden'
    id_detalle = db.Column(db.Integer, primary_key=True, autoincrement=True)
    id_orden = db.Column(db.Integer, db.ForeignKey('ordenes.id_orden'), nullable=False)
    id_repuesto = db.Column(db.Integer, db.ForeignKey('repuestos.id_repuesto'), nullable=False)
    cantidad = db.Column(db.Integer, nullable=False)
    precio = db.Column(db.Float, nullable=False)

    def __init__(self, id_orden, id_repuesto, cantidad, precio):
        self.id_orden = id_orden
        self.id_repuesto = id_repuesto
        self.cantidad = cantidad
        self.precio = precio

class ModeloSchema(ma.SQLAlchemyAutoSchema):
    class Meta:
        model = Modelo

# *-METODOS PARA LOGIN Y REGISTER-*

# Validando si existe el usuario y si conincide con la contraseña, y devuelve si es cliente o admin.
@app.route('/validar_usuario', methods=['POST'])
def validar_usuario():
    datos = request.json
    print(f"Datos recibidos: {datos}")  # Log para verificar los datos recibidos
    usuario = datos.get('usuario')
    contrasena = datos.get('contrasena')

    # Buscar el usuario en la base de datos
    usuario_encontrado = Usuario.query.filter_by(usuario=usuario, contrasena=contrasena).first()

    if usuario_encontrado:
        print(f"Usuario encontrado: {usuario_encontrado.usuario}")  # Log para verificar el usuario encontrado
        return jsonify({"success": True, "tipo_usuario": usuario_encontrado.tipo_usuario})
    else:
        print("Usuario o contraseña incorrectos")  # Log para verificar que el usuario no fue encontrado
        return jsonify({"success": False})

# Endpoint para registrar un usuario, pide al usuario su nombre de usuario y su contrasena. Por defecto en tipo_usuario se pone 'cliente'.
@app.route('/registrar_usuario', methods=['POST'])
def registrar_usuario():
    usuario = request.json['usuario']
    contrasena = request.json['contrasena']
    
    # Asegurar que el tipo_usuario sea 'cliente' por defecto
    nuevo_usuario = Usuario(usuario=usuario, contrasena=contrasena, tipo_usuario='cliente')
    
    try:
        db.session.add(nuevo_usuario)
        db.session.commit()
        return jsonify({"mensaje": "Usuario registrado exitosamente", "id_usuario": nuevo_usuario.id_usuario}), 201
    except Exception as e:
        return jsonify({"mensaje": "Error al registrar usuario", "error": str(e)}), 500

# MÉTODO PARA TABLA MARCA, DEVUELVE TODAS LAS MARCAS
@app.route('/marcas', methods=['GET'])
def obtener_marcas():
    try:
        marcas = Marca.query.all()
        resultado = [{"id_marca": marca.id_marca, "nombre_marca": marca.nombre_marca} for marca in marcas]
        return jsonify(resultado), 200
    except Exception as e:
        return jsonify({"mensaje": "Error al obtener marcas", "error": str(e)}), 500

# MÉTODO PARA RECIBIR LOS MODELOS SEGÚN EL id_marca. No devuelve los id's
@app.route('/modelos/<int:id_marca>', methods=['GET'])
def get_modelos_by_marca(id_marca):
    modelos = Modelo.query.filter_by(id_marca=id_marca).all()

    if modelos:
        # Construir la respuesta manualmente sin usar modelos_schema
        modelos_sin_ids = [{'nombre_modelo': modelo.nombre_modelo} for modelo in modelos]
        return jsonify(modelos_sin_ids)
    else:
        return jsonify({'mensaje': 'No se encontraron modelos para la marca proporcionada'})


# Devuelve el tipo de repuestos que existen
@app.route('/tipos-repuestos', methods=['GET'])
def get_tipos_repuestos():
    tipos_repuestos = TipoRepuesto.query.with_entities(TipoRepuesto.nombre_tipo_repuesto).all()

    if tipos_repuestos:
        nombres_tipos_repuestos = [tipo.nombre_tipo_repuesto for tipo in tipos_repuestos]
        return jsonify(nombres_tipos_repuestos)
    else:
        return jsonify({'mensaje': 'No se encontraron tipos de repuestos'})

# Repuestos, filtra por id_modelo o id_tipo_repuesto
@app.route('/repuestos', methods=['GET'])
def get_repuestos():
    id_modelo = request.args.get('id_modelo')
    id_tipo_repuesto = request.args.get('id_tipo_repuesto')

    query = Repuesto.query

    if id_modelo:
        query = query.filter_by(id_modelo=id_modelo)
    if id_tipo_repuesto:
        query = query.filter_by(id_tipo_repuesto=id_tipo_repuesto)

    repuestos = query.all()

    if repuestos:
        resultado = []
        for repuesto in repuestos:
            resultado.append({
                'nombre_repuesto': repuesto.nombre_repuesto,
                'precio': repuesto.precio,
                'stock': repuesto.stock,
                'tipo_repuesto': repuesto.tipo_repuesto.nombre_tipo_repuesto,
                'modelo': repuesto.modelo.nombre_modelo
            })
        return jsonify(resultado)
    else:
        return jsonify({'mensaje': 'No se encontraron repuestos para los parámetros proporcionados'})

# Carrito
@app.route('/agregar_carrito', methods=['POST'])
def agregar_carrito():
    datos = request.json
    id_usuario = datos.get('id_usuario')
    
    if not id_usuario:
        return jsonify({"mensaje": "El id_usuario es requerido"}), 400

    # Verificar si el usuario existe
    usuario = Usuario.query.get(id_usuario)
    if not usuario:
        return jsonify({"mensaje": "Usuario no encontrado"}), 404

    # Crear un nuevo carrito
    nuevo_carrito = Carrito(id_usuario=id_usuario)

    try:
        db.session.add(nuevo_carrito)
        db.session.commit()
        return jsonify({"mensaje": "Carrito agregado exitosamente", "id_carrito": nuevo_carrito.id_carrito}), 201
    except Exception as e:
        return jsonify({"mensaje": "Error al agregar carrito", "error": str(e)}), 500

# Agregar detalle al carrito
@app.route('/agregar_detalle', methods=['POST'])
def agregar_detalle():
    datos = request.json
    id_carrito = datos.get('id_carrito')
    id_repuesto = datos.get('id_repuesto')
    cantidad = datos.get('cantidad')
    
    if not id_carrito or not id_repuesto or not cantidad:
        return jsonify({"mensaje": "Los campos id_carrito, id_repuesto y cantidad son requeridos"}), 400

    # Verificar si el carrito existe
    carrito = Carrito.query.get(id_carrito)
    if not carrito:
        return jsonify({"mensaje": "Carrito no encontrado"}), 404

    # Verificar si el repuesto existe
    repuesto = Repuesto.query.get(id_repuesto)
    if not repuesto:
        return jsonify({"mensaje": "Repuesto no encontrado"}), 404

    if repuesto.stock < cantidad:
        return jsonify({"mensaje": "No hay suficiente stock para el repuesto"}), 400

    # Crear un nuevo detalle de carrito
    nuevo_detalle = DetalleCarrito(id_carrito=id_carrito, id_repuesto=id_repuesto, cantidad=cantidad)

    # Restar la cantidad del stock
    repuesto.stock -= cantidad

    try:
        db.session.add(nuevo_detalle)
        db.session.commit()
        return jsonify({"mensaje": "Detalle agregado exitosamente", "id_detalle": nuevo_detalle.id_detalle}), 201
    except Exception as e:
        return jsonify({"mensaje": "Error al agregar detalle", "error": str(e)}), 500


# Crear Orden
@app.route('/crear_orden', methods=['POST'])
def crear_orden():
    datos = request.json
    id_usuario = datos.get('id_usuario')

    # Obtener el carrito del usuario
    carrito = Carrito.query.filter_by(id_usuario=id_usuario).first()
    if not carrito:
        return jsonify({"mensaje": "No se encontró el carrito para el usuario proporcionado"}), 404

    # Obtener detalles del carrito
    detalles = DetalleCarrito.query.filter_by(id_carrito=carrito.id_carrito).all()
    
    if not detalles:
        return jsonify({"mensaje": "El carrito está vacío"}), 400

    total = 0
    for detalle in detalles:
        repuesto = Repuesto.query.filter_by(id_repuesto=detalle.id_repuesto).first()
        if repuesto:
            total += detalle.cantidad * repuesto.precio
            # Actualizar el stock del repuesto
            repuesto.stock -= detalle.cantidad
            if repuesto.stock < 0:
                return jsonify({"mensaje": "Stock insuficiente para el repuesto: {}".format(repuesto.nombre_repuesto)}), 400
    # Crear la orden
    nueva_orden = Orden(id_usuario=id_usuario, total=total)
    try:
        db.session.add(nueva_orden)
        db.session.commit()

        # Crear los detalles de la orden
        for detalle in detalles:
            repuesto = Repuesto.query.filter_by(id_repuesto=detalle.id_repuesto).first()
            if repuesto:
                detalle_orden = DetalleOrden(
                    id_orden=nueva_orden.id_orden,
                    id_repuesto=detalle.id_repuesto,
                    cantidad=detalle.cantidad,
                    precio=repuesto.precio
                )
                db.session.add(detalle_orden)

        db.session.commit()

        return jsonify({"mensaje": "Orden creada exitosamente", "id_orden": nueva_orden.id_orden}), 201
    except Exception as e:
        db.session.rollback()
        return jsonify({"mensaje": "Error al crear la orden", "error": str(e)}), 500

# Ordenando de mayor a menor
@app.route('/repuestosprecio', methods=['GET'])
def get_repuestosprecios():
    # Obtiene el parámetro 'order' de la solicitud, por defecto 'desc' (de mayor a menor)
    order = request.args.get('order', 'desc').lower()
    
    if order not in ['asc', 'desc']:
        return jsonify({'error': 'Invalid order parameter. Use "asc" or "desc".'}), 400
    
    conn = get_db_connection()
    cursor = conn.cursor()
    
    # Ejecuta la consulta SQL para obtener repuestos ordenados por precio
    query = 'SELECT * FROM repuestos ORDER BY precio {}'.format(order)
    cursor.execute(query)
    
    repuestos = cursor.fetchall()
    conn.close()
    
    # Convertir los resultados en una lista de diccionarios
    repuestos_list = [dict(row) for row in repuestos]
    
    return jsonify(repuestos_list)

#Ordenando por marca los modelos
@app.route('/modelos/marca/<int:id_marca>', methods=['GET'])
def get_modelos_por_marca(id_marca):
    try:
        modelos = Modelo.query.filter_by(id_marca=id_marca).all()
        if modelos:
            modelo_schema = ModeloSchema(many=True)
            return modelo_schema.jsonify(modelos)
        else:
            return jsonify({'message': 'No models found for this brand.'}), 404
    except Exception as e:
        print(f"Error: {e}")
        return jsonify({'error': 'Internal Server Error'}), 500
    


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)

#  python apidb.py, para ejecutar