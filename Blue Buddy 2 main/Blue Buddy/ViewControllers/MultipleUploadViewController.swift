//
//  MultipleUploadViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/4/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import OpalImagePicker
//import ImagePicker
//import Lightbox
class MultipleUploadViewController: UIViewController, OpalImagePickerControllerDelegate, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {

    @IBOutlet weak var collectionViewImg: UICollectionView!
   
    var productData = ProductDetailsBO()
    var arrImg = [UIImage]()
    var indexVal = Int()
    override func viewDidLoad() {
        super.viewDidLoad()
        let layout: UICollectionViewFlowLayout = UICollectionViewFlowLayout()
        layout.sectionInset = UIEdgeInsets(top: 0, left: 2, bottom: 0, right: 2)
        layout.minimumInteritemSpacing = 0
        layout.minimumLineSpacing = 5
        collectionViewImg!.collectionViewLayout = layout
        collectionViewImg.delegate = self
        collectionViewImg.dataSource = self
        if !productData.product_id.isEmpty
        {
            collectionViewImg.reloadData()
        }
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated : true)
    }
    @IBAction func onClickBtnNext(_ sender: UIButton) {
        for i in 0..<productData.arrImages.count
        {
            let imgData = productData.arrImages[i] as! ImageBO
            if imgData.locImg != nil
            {
                arrImg.append(imgData.locImg!)
            }
        }

        if arrImg.count == 0 && productData.product_id.isEmpty
        {
            Common.showAlert(message: "Please select your product picture", title: "BLUE BUDDY", viewController: self)
        }
        else{
            let addProductVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AddProductViewController") as! AddProductViewController
            addProductVC.arrImages = arrImg
            addProductVC.indexVal = self.indexVal
            if !productData.product_id.isEmpty
            {
                addProductVC.productData = productData
            }
            self.navigationController?.pushViewController(addProductVC, animated: true)
        }
    }


    
    func imagePicker(_ picker: OpalImagePickerController, didFinishPickingImages images: [UIImage])
    {
        let arrVal = NSMutableArray()
        for i in 0..<images.count
        {
            let imgD = ImageBO()
            imgD.locImg = images[i]
            arrVal.add(imgD)
        }
        productData.arrImages.addObjects(from: arrVal as! [Any])
        collectionViewImg.reloadData()
        
        dismiss(animated: true, completion: nil)
    }
    func imagePickerDidCancel(_ picker: OpalImagePickerController){
//        dismiss(animated: true, completion: nil)
        
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: CGFloat((collectionView.frame.size.width / 4) - 5), height: CGFloat(100))
    }
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    //2
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 4
    }
    
    //3
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "img_Cell",
                                                      for: indexPath) as! ImgCollectionViewCell
        if indexPath.row < productData.arrImages.count
        {
            let imgData: ImageBO = productData.arrImages[indexPath.row] as! ImageBO
            if !imgData.image_name.isEmpty
            {
                cell.imgProduct.kf.setImage(with: URL(string:imgData.image_name), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
            }
            else{
                cell.imgProduct.image = imgData.locImg
            }
            cell.btnAdd.isHidden = true
            cell.btnDelete.isHidden = false
            if productData.arrImages.count == 1
            {
                cell.btnAdd.isHidden = true
                cell.btnDelete.isHidden = true
            }
        }
        else{
            cell.imgProduct.backgroundColor = .lightGray
            cell.imgProduct.image = nil
            cell.btnAdd.isHidden = false
            cell.btnDelete.isHidden = true
        }
        cell.onClickDelete = {() -> Void in
            let imgData: ImageBO = self.productData.arrImages[indexPath.row] as! ImageBO
            let alert = UIAlertController(title: "BLUE BUDDY", message: "Do you want to delete this image?", preferredStyle: UIAlertControllerStyle.alert)
            let okAction = UIAlertAction(title: NSLocalizedString("Yes", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                DispatchQueue.main.async {
                    if !imgData.image_name.isEmpty
                    {
                        MarketParser.callDeleteProductImageWebService(imgData.image_id, complete: { (status, strMsg) in
                            if status
                            {
                                DispatchQueue.main.async {
                                    self.productData.arrImages.remove(imgData)
                                    self.collectionViewImg.reloadData()
                                }
                            }
                            else{
                                Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                            }
                        })
                    }
                    else
                    {
                        self.productData.arrImages.remove(imgData)
                        self.collectionViewImg.reloadData()
                    }
                }
            })
            let cancelAction = UIAlertAction(title: NSLocalizedString("NO", comment: "No action"), style: .cancel, handler: nil)
            alert.addAction(okAction)
            alert.addAction(cancelAction)
            self.present(alert, animated: true, completion: nil)
        }
        cell.onClickAdd = {() -> Void in
            let imagePicker = OpalImagePickerController()
            imagePicker.imagePickerDelegate = self
            if !self.productData.product_id.isEmpty
            {
                imagePicker.maximumSelectionsAllowed = 4 - self.productData.arrImages.count
            }
            else{
                
                imagePicker.maximumSelectionsAllowed = 4
            }
            imagePicker.allowedMediaTypes = Set([.image])
            self.present(imagePicker, animated: true, completion: nil)
        }
        // Configure the cell
        return cell
    }

    
//    func wrapperDidPress(_ imagePicker: ImagePickerController, images: [UIImage])
//    {
//        guard images.count > 0 else { return }
//        
//        let lightboxImages = images.map {
//            return LightboxImage(image: $0)
//        }
//        
//        let lightbox = LightboxController(images: lightboxImages, startIndex: 0)
//        imagePicker.present(lightbox, animated: true, completion: nil)
//
//    }
//    
//    func doneButtonDidPress(_ imagePicker: ImagePickerController, images: [UIImage])
//    {
//        imagePicker.dismiss(animated: true, completion: nil)
//
//    }
//    func cancelButtonDidPress(_ imagePicker: ImagePickerController)
//    {
//        imagePicker.dismiss(animated: true, completion: nil)
//
//    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
