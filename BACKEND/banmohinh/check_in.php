<?php
include "connect.php";
$id=$_POST['id'];

//lay ngay hien tai
$query = 'SELECT CURDATE()';
$data = mysqli_query($conn, $query);
$row = mysqli_fetch_assoc($data);
$dateCurrent = $row['CURDATE()'];

//lay ngay user da login trước
$query = "SELECT `last_login` FROM `tbl_account` WHERE `account_id` =" .$id;
$data = mysqli_query($conn, $query);
$row = mysqli_fetch_assoc($data);
$last_login = $row['last_login'];

if (strtotime($dateCurrent) === strtotime($last_login)) {
    //2 ngày giống nhau
    $arr = [
        'success' => true,
        'message' => "Thành công"
    ];
} else {
    //cập nhật lượt điểm danh, luckybox mới và ngày login mới
    $query = 'UPDATE `tbl_account` SET `check_in`= 1,`luckybox`= 3,`last_login`="'.$dateCurrent.'" WHERE `account_id` = '.$id;
    $data = mysqli_query($conn, $query);

    if ($data == true){
        $arr = [
            'success' => true,
            'message' => "Thành công"
        ];
    } else {
        $arr = [
            'success' => false,
            'message' => "Không thành công"
        ];
    }
}

print_r(json_encode($arr));
?>