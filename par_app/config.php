<?php

#funcion para la conexion a base de datos
function conectar() {
    $server = "localhost";
    $database = "actividades";
    $user = "root";
    $password = "";
    // Create connection
    $conexion = mysqli_connect($server, $user, $password, $database);
    $conexion->set_charset("utf8mb4");

    // Check connection
    if (!$conexion) {
       $conexion = mysqli_error($conexion);
    }

    return $conexion;
}

# Funcion para desconectar
function desconectar($conexion) {
    try {
        mysqli_close($conexion);
        $estado = true;
    } catch (Exception $e) {
        $estado = false;
    }
    return $estado;
}

# Insertando datos a la BD
function insertar($nombre, $descripcion,$fecha,$hora,$estado) {
    $conexion = conectar();
    $query = "INSERT INTO tareas (id_tarea, nombre,descripcion,fecha,hora,estado) VALUES(NULL, '$nombre', '$descripcion','$fecha',
    '$hora','$estado')";   
    
    if (mysqli_query($conexion, $query)) {
        $estado = true; 
    }else {
        $estado = false;
    }

    desconectar($conexion);
    return $estado;
}

# Actualizando datos en la BD
function modificar($id_tarea, $nombre, $descripcion,$fecha,$hora,$estado) {
    $conexion = conectar();
    $query = "UPDATE tareas  SET nombre = '$nombre', descripcion = '$descripcion', fecha = '$fecha',hora  = '$hora',estado = '$estado' 
    WHERE id_tarea = '$id_tarea'";   
    
    if (mysqli_query($conexion, $query)) {
        $estado = true; 
    }else {
        $estado = false;
    }

    desconectar($conexion);
    return $estado;
}

# Eliminando datos de la BD
function eliminar($id_tarea) {
    $conexion = conectar();
    $query = "DELETE FROM tareas WHERE id_tarea = '$id_tarea'";   
    
    if (mysqli_query($conexion, $query)) {
        $estado = true; 
    }else {
        $estado = false;
    }

    desconectar($conexion);
    return $estado;
}

# Eliminando actividades
function listar_actividades($filtro) {
    $conexion = conectar();
    $json = array();
    
    $query = "SELECT id_tarea, nombre, descripcion,fecha,hora,estado FROM tareas WHERE  estado LIKE '%$filtro%'";
    
    $result = mysqli_query($conexion, $query);
    
    if (mysqli_num_rows($result) > 0) {
        while ($contacto = mysqli_fetch_array($result)) {
            $row = array();
            $row['id_tarea'] = $contacto['id_tarea'];
            $row['nombre'] = $contacto['nombre'];
            $row['descripcion'] = $contacto['descripcion'];
            $row['fecha'] = $contacto['fecha'];
            $row['hora'] = $contacto['hora'];
            $row['estado'] = $contacto['estado'];
            $json[] = $row;
        }
    }
    
    desconectar($conexion);
    return array_values($json);
}