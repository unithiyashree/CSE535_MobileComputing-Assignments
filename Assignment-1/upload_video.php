<?php 

$file_path = "files/";
$group  = "G" . $_POST['group_id'] . "/";
$asu_id = $_POST['id'] . "/";
$accept = $_POST['accept'];

if($accept == 1){
    $file_path = $file_path . $group . $asu_id . "accept/";
} else {
    $file_path = $file_path . $group . $asu_id . "reject/";
}

if(!file_exists($file_path)){
    mkdir($file_path, 0777, true);
}

$file = $file_path . basename($_FILES['uploaded_file']['name']);
echo $file;
if (move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file)) {
    echo "success";
} else {
    echo "fail";
}
