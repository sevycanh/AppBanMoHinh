<?php
include "connect.php";

$order_id = $_POST['order_id'];

$query = "SELECT * FROM `tbl_orderdetail` WHERE `order_id` = '$order_id'" ;

//echo $query;
$data = mysqli_query($conn, $query);
$result = array();
while ($row = mysqli_fetch_assoc($data)) {
    // code...
    $result[] = ($row);
}
if (!empty($result)){

    foreach($result as $key => $value){
        $query_update = 'UPDATE  `tbl_product` SET `quantity` = `quantity` + '.intval($value["quantity"]).' WHERE `product_id`= '.$value["product_id"].'';
        $data = mysqli_query($conn, $query_update);
        print_r(json_encode($data));

    }
	if ($data == true){
        $arr = [
            'success' => true,
            'message' => "thanh cong",
        ];
    } else {
        $arr = [
            'success' => false,
            'message' => "Khong thanh cong",
        ];
    }
    print_r(json_encode($arr));
} 
else {
	$arr = [
		'success' => false,
		'message' => "Loi ket noi",
        'result' => $result

	];
	print_r(json_encode($arr));
}

?>