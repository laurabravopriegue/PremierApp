<?php
    $con = mysqli_connect("localhost", "id451584_quizapp", "submeseta", "id451584_premier");
    mysqli_set_charset($con, 'utf8');
    $check = "SELECT username, maxscore FROM user WHERE maxscore > 0 ORDER BY maxscore DESC;";
    $rs = mysqli_query($con,$check);
    $response = array();
    while ($data = mysqli_fetch_array($rs, MYSQLI_ASSOC)) 
    {
        $response[] = $data;
    }
    echo json_encode($response);
?>