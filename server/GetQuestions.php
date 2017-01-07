<?php
    $con = mysqli_connect("localhost", "id451584_quizapp", "submeseta", "id451584_premier");
    $check = "SELECT * FROM question";
    $rs = mysqli_query($con,$check);
    $response = array();
    while ($data = mysqli_fetch_array($rs, MYSQLI_ASSOC)) 
    {
        $response[] = $data;
    }
    echo json_encode($response);
?>