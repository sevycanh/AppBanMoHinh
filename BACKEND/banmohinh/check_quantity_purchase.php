<?php
include "connect.php";

$purchase = $_POST['purchase'];
// $value["idProduct"].','.$value["quantity"]

$purchase = json_decode($purchase, true);
foreach($purchase as $key => $value){
    $query = 'SELECT * FROM `tbl_product` WHERE `product_id` = '.$value["idProduct"].' AND `quantity` >= '.$value["quantity"].'';
    $data = mysqli_query($conn, $query);
    $result = array();
    while ($row = mysqli_fetch_assoc($data)) {
        // code...
        $result[] = ($row);
    }
    if (!empty($result)){
        $arr = [
            'success' => true,
            'message' => "thanh cong",
        ];
    } else {
        $arr = [
            'success' => false,
            'message' => "Sản phẩm thứ " . $key+1 . " bạn chọn mua vượt quá số lượng có sẵn!",
        ];
        print_r(json_encode($arr));
        return ;
    }
}

foreach($purchase as $key => $value){
    $query = 'UPDATE  `tbl_product` SET `quantity` = `quantity` - '.$value["quantity"].' WHERE `product_id`= '.$value["idProduct"].'' ;
    $data = mysqli_query($conn, $query);
    if ($data == true){
		$arr = [
			'success' => true,
			'message' => "Thanh Cong",
		];
	} 
	else {
		$arr = [
			'success' => false,
			'message' => "That Bai",
		];
	}
}

print_r(json_encode($arr));

?>