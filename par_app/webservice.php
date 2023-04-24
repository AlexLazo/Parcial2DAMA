<?php
    require_once "config.php";

    $datos = array();
    $accion = "";

    if (isset($_POST["accion"])) {
        $accion = $_POST["accion"];
    }

    if ($accion == "insertar") {
        $nombre = $_POST["nombre"];
        $descripcion = $_POST["descripcion"];
        $fecha = $_POST["fecha"];
        $hora = $_POST["hora"];
        $estado = $_POST["estado"];

        if (insertar($nombre, $descripcion,$fecha,$hora,$estado) == true) {
            $datos["estado"] = true;
            $datos["resultado"] = "Se realizó el registro";
        }else {
            $datos["estado"] = false;
            $datos["resultado"] = "No se pudo almacenar el contacto";
        }
    }else if ($accion == "listar_actividades") {
        $filtro = (isset($_POST["filtro"])) ? $_POST["filtro"] : "";
        $datos["estado"] = true;
        $datos["resultado"] = listar_actividades($filtro);
    }else if ($accion == "modificar") {
        $id_tarea = $_POST["id_tarea"];
        $nombre = $_POST["nombre"];
        $descripcion = $_POST["descripcion"];
        $fecha = $_POST["fecha"];
        $hora = $_POST["hora"];
        $estado = $_POST["estado"];

        if (modificar($id_tarea, $nombre, $descripcion,$fecha,$hora,$estado) == true) {
            $datos["estado"] = true;
            $datos["resultado"] = "Registro actualizado correctamente";
        }else {
            $datos["estado"] = false;
            $datos["resultado"] = "No se pudo actualizar el contacto";
        }
    }else if ($accion == "eliminar") {
        $id_tarea = $_POST["id_tarea"];

        if (eliminar($id_tarea) == true) {
            $datos["estado"] = true;
            $datos["resultado"] = "Registro eliminado correctamente";
        }else {
            $datos["estado"] = false;
            $datos["resultado"] = "No se pudo eliminar el contacto";
        }
    } 

    echo json_encode($datos);
?> 