//
//  MarketParser.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/12/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class MarketParser: NSObject {

    
    // MARK: -
    // MARK: - Charter List Service
    class func callChaterListService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList : NSArray) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Charter/list") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                MarketParser.parseCharterList(dictResult, complete: { (status, strMsg, arrList) in
                    completeBlock(status, strMsg, arrList)
                })
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", [])
            }
        }
    }
    
    class func parseCharterList(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let arrTripList: NSMutableArray?
        arrTripList = []
        if status!{
            let arrList :NSArray = dictResult.value(forKey: "charter_list") as! NSArray
            
            for index in 0 ..< arrList.count
            {
                let dictDetails : NSDictionary = arrList.object(at: index) as! NSDictionary
                let charterData = CharterDetailsBO()
                let activity                   = dictDetails.value(forKey: "charter_nm") as! String
                charterData.name               = activity.capitalized
                //image
                let imgName:String = dictDetails.value(forKey: "profile_pic") as! String
                charterData.user_pic           = imgUrl + imgName
                let imgCharName:String = dictDetails.value(forKey: "image") as! String
                charterData.charter_image      = imgUrl + imgCharName
                charterData.user_email         = dictDetails.value(forKey: "email") as! String
                let fname                      = dictDetails.value(forKey: "fname") as! String
                let lname                      = dictDetails.value(forKey: "lname") as! String
                charterData.user_name          = fname.capitalized + " " + lname.capitalized
                charterData.charter_id         = dictDetails.value(forKey: "id")as! String
                let editVal                    = dictDetails.value(forKey: "is_editable")as! String
                charterData.is_editable        = editVal == "1" ? true : false
                charterData.user_loc           = dictDetails.value(forKey: "location")as! String
                charterData.user_id            = dictDetails.value(forKey: "user_id")as! String
                charterData.price              = dictDetails.value(forKey: "price")as! String
                let flagged                    = dictDetails.value(forKey: "is_flagged")as! String
                charterData.is_flagged         = flagged == "1" ? true : false
                let featured                   = dictDetails.value(forKey: "featured")as! String
                charterData.is_featured        = featured == "1" ? true : false
                charterData.firebase_id        = dictDetails.value(forKey: "firebase_id") as! String
                arrTripList?.add(charterData)
            }
        }
        completeBlock(status!, "", arrTripList!)
    }
    
    // MARK: -
    // MARK: - Charter Details Service
    class func callChaterDetailsService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ objList : CharterDetailsBO) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "charter/detail") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                MarketParser.parseCharterDetails(dictResult, complete: { (status, strMsg, objValue) in
                    completeBlock(status, strMsg, objValue)
                })
            }
            else{
                let objval = CharterDetailsBO()
                completeBlock(isSuccess, "Something Went Wrong", objval)
            }
        }
    }
    
    class func parseCharterDetails(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ objDetails: CharterDetailsBO) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let charterData = CharterDetailsBO()
        if status!{
                let dictDetails : NSDictionary = dictResult.value(forKey: "details") as! NSDictionary
            
                let activity                   = dictDetails.value(forKey: "name") as! String
                charterData.name               = activity.capitalized
                charterData.capacity           = dictDetails.value(forKey: "capacity") as! String
                charterData.charter_desc       = dictDetails.value(forKey: "description") as! String
                charterData.half_price         = dictDetails.value(forKey: "half_price") as! String
                charterData.user_email         = dictDetails.value(forKey: "email") as! String
                let fname                      = dictDetails.value(forKey: "fname") as! String
                let lname                      = dictDetails.value(forKey: "lname") as! String
                charterData.user_name          = fname.capitalized + " " + lname.capitalized
                charterData.user_loc           = dictDetails.value(forKey: "location")as! String
                charterData.price              = dictDetails.value(forKey: "full_price")as! String
                charterData.rating             = dictDetails.value(forKey: "rating") as! Int
                charterData.size               = dictDetails.value(forKey: "size") as! String
                charterData.boat_type          = dictDetails.value(forKey: "type_of_boat") as! String
                let imgName:String = dictDetails.value(forKey: "image") as! String
                charterData.charter_image      = imgUrl + imgName
                charterData.user_Phone         = dictDetails.value(forKey: "phone") as! String
                let featured                   = dictDetails.value(forKey: "featured")as! String
                charterData.is_featured        = featured == "1" ? true : false
                charterData.charter_id         = dictDetails.value(forKey: "id")as! String
                charterData.user_id            = dictDetails.value(forKey: "user_id")as! String
                charterData.firebase_id        = dictDetails.value(forKey: "firebase_reg") as! String
                let editVal                    = dictDetails.value(forKey: "is_reviewed")as! String
            
            let hide_details                      = dictDetails.value(forKey: "hide_details")as? String != nil ? dictDetails.value(forKey: "hide_details")as! String : ""
            charterData.is_hidden          = hide_details == "1" ? true : false
            
                charterData.is_reviewed        = editVal == "1" ? true : false
                let arrList :NSArray = dictResult.value(forKey: "review") as! NSArray
                for index in 0 ..< arrList.count
                {
                    let dictDetails : NSDictionary = arrList.object(at: index) as! NSDictionary
                    let reviewData = ReviewBO()
                    let fname                       = dictDetails.value(forKey: "reviewer_fname") as! String
                    let lname                       = dictDetails.value(forKey: "reviewer_lname") as! String
                    reviewData.user_name            = fname.capitalized + " " + lname.capitalized
                    reviewData.rating               = dictDetails.value(forKey: "rating") as! String
                    reviewData.user_id              = dictDetails.value(forKey: "user_id")as! String
                    reviewData.review_title         = dictDetails.value(forKey: "title")as! String
                    reviewData.review_id            = dictDetails.value(forKey: "id")as! String
                    reviewData.reviewDesc           = dictDetails.value(forKey: "description") as! String
                    reviewData.date                 = dictDetails.value(forKey: "date") as! String
                    let imgName:String = dictDetails.value(forKey: "profile_pic") as! String
                    reviewData.user_pic             = imgUrl + imgName
                    charterData.arrReviews.add(reviewData)
            }
        }
        completeBlock(status!, "", charterData)
    }
    
    // MARK: -
    // MARK: - Create Charter Service
    class func callCreateCharterService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ strCharterId: String) -> Void) {
        BaseParser.callMultiPartDataWebService(serverUrl: APP_SERVICE(str: "Charter/create/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess
            {
                print(dictResult)
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                var strId = ""
                if status!
                {
                    strId = (dictResult.value(forKey: "id") as? String)!
                }

                completeBlock(status!, strMsg!, strId)
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", "")
            }
        }
    }
    // MARK: -
    // MARK: - Update Review Service
    class func callUpdateReviewService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Review/update/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess
            {
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                
                
                completeBlock(status!, strMsg!)

            }
            else{
                completeBlock(isSuccess, "Something Went Wrong")
            }
        }
    }

    
    // MARK: -
    // MARK: - Create Review Service
    class func callCreateReviewService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ objReviewData: ReviewBO) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Review/create/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess
            {
                MarketParser.parseReviewDetails(dictResult, complete: { (status, strMsg, objValue) in
                    completeBlock(status, strMsg, objValue)
                })
            }
            else{
                let reviewData = ReviewBO()
                completeBlock(isSuccess, "Something Went Wrong", reviewData)
            }
        }
    }
    class func parseReviewDetails(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ objDetails: ReviewBO) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let strMsg = dictResult.value(forKey: "message") as? String
        let reviewData = ReviewBO()
        if status!{
            let arrList :NSArray = dictResult.value(forKey: "review") as! NSArray
            let dictDetails : NSDictionary = arrList.firstObject as! NSDictionary
            
            let fname                       = dictDetails.value(forKey: "reviewer_fname") as! String
            let lname                       = dictDetails.value(forKey: "reviewer_lname") as! String
            reviewData.user_name            = fname.capitalized + " " + lname.capitalized
            reviewData.rating               = dictDetails.value(forKey: "rating") as! String
            reviewData.user_id              = dictDetails.value(forKey: "user_id")as! String
            reviewData.review_title         = dictDetails.value(forKey: "title")as! String
            reviewData.review_id            = dictDetails.value(forKey: "id")as! String
            reviewData.reviewDesc           = dictDetails.value(forKey: "description") as! String
            reviewData.date                 = dictDetails.value(forKey: "date") as! String
            let imgName:String = dictDetails.value(forKey: "profile_pic") as! String
            reviewData.user_pic             = imgUrl + imgName

        }
        
        completeBlock(status!, strMsg!, reviewData)
    }
    
    // MARK: -
    // MARK: - Update Charter Service
    class func callUpdateCharterService(_ dictPram: NSDictionary, _isFromPic: Bool, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        if _isFromPic
        {
            BaseParser.callMultiPartDataWebService(serverUrl: APP_SERVICE(str: "Charter/update/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
                if isSuccess
                {
                    print(dictResult)
                    let status = dictResult.value(forKey: "status") as? Bool
                    let strMsg = dictResult.value(forKey: "message") as? String
                    completeBlock(status!, strMsg!)
                }
                else{
                    completeBlock(isSuccess, "Something Went Wrong")
                }
            }
        }
        else{
            BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Charter/update/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
                if isSuccess
                {
                    let status = dictResult.value(forKey: "status") as? Bool
                    let strMsg = dictResult.value(forKey: "message") as? String
                    completeBlock(status!, strMsg!)
                }
                else{
                    completeBlock(isSuccess, "Something Went Wrong")
                }
            }
        }
    }
    
    // MARK: -
    // MARK: - Course List Service
    class func callCourseListService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList : NSArray) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Course/list") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                MarketParser.parseCourseList(dictResult, complete: { (status, strMsg, arrList) in
                    completeBlock(status, strMsg, arrList)
                })
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", [])
            }
        }
    }
    class func parseCourseList(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let arrCourseList: NSMutableArray?
        arrCourseList = []
        if status!{
            let arrList :NSArray = dictResult.value(forKey: "course_list") as! NSArray
            
            for index in 0 ..< arrList.count
            {
                let dictDetails : NSDictionary = arrList.object(at: index) as! NSDictionary
                let courseData = CoursesDetailsBO()
                let imgName:String = dictDetails.value(forKey: "profile_pic") as! String
                courseData.user_pic           = imgUrl + imgName
                let imgCharName:String = dictDetails.value(forKey: "image") as! String
                courseData.course_image       = imgUrl + imgCharName
                courseData.user_email         = dictDetails.value(forKey: "email") as! String
                courseData.category           = dictDetails.value(forKey: "category") as! String
                let fname                     = dictDetails.value(forKey: "fname") as! String
                let lname                     = dictDetails.value(forKey: "lname") as! String
                courseData.user_name          = fname.capitalized + " " + lname.capitalized
                courseData.course_id          = dictDetails.value(forKey: "id")as! String
                let editVal                   = dictDetails.value(forKey: "is_editable")as! String
                courseData.is_editable        = editVal == "1" ? true : false
                courseData.user_loc           = dictDetails.value(forKey: "location")as! String
                courseData.user_id            = dictDetails.value(forKey: "user_id")as! String
                courseData.price              = dictDetails.value(forKey: "price")as! String
                let flagged                    = dictDetails.value(forKey: "is_flagged")as! String
                courseData.is_flagged         = flagged == "1" ? true : false
                let featured                   = dictDetails.value(forKey: "featured")as! String
                courseData.is_featured        = featured == "1" ? true : false
                courseData.firebase_id        = dictDetails.value(forKey: "firebase_id") as! String
                arrCourseList?.add(courseData)
            }
        }
        completeBlock(status!, "", arrCourseList!)
    }

    
    // MARK: -
    // MARK: - Create Course Service
    class func callCreateCourseService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ strId :String) -> Void) {
        BaseParser.callMultiPartDataWebService(serverUrl: APP_SERVICE(str: "Course/create/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                var strId = ""
                if status!
                {
                    strId = (dictResult.value(forKey: "id") as? String)!
                }
                
                completeBlock(status!, strMsg!, strId)            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", "")
            }
        }
    }

    // MARK: -
    // MARK: - Course Details Service
    class func callCourseDetailsService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ objList : CoursesDetailsBO) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Course/detail") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                MarketParser.parseCourseDetails(dictResult, complete: { (status, strMsg, objValue) in
                    completeBlock(status, strMsg, objValue)
                })
            }
            else{
                let objval = CoursesDetailsBO()
                completeBlock(isSuccess, "Something Went Wrong", objval)
            }
        }
    }

    class func parseCourseDetails(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ objDetails: CoursesDetailsBO) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let courseData = CoursesDetailsBO()
        if status!{
            let dictDetails : NSDictionary = dictResult.value(forKey: "details") as! NSDictionary
            
            let activity                    = dictDetails.value(forKey: "category") as! String
            courseData.name                 = activity.capitalized
            courseData.course_desc          = dictDetails.value(forKey: "description") as! String
            courseData.agency_name          = dictDetails.value(forKey: "agency_name") as! String
            courseData.agency_url           = dictDetails.value(forKey: "agency_url") as! String
            courseData.duration             = dictDetails.value(forKey: "duration") as! String
            courseData.price                = dictDetails.value(forKey: "price") as! String
            courseData.user_email           = dictDetails.value(forKey: "email") as! String
            courseData.user_loc             = dictDetails.value(forKey: "location")as! String
            courseData.rating               = dictDetails.value(forKey: "rating") as! Int
            let imgName:String              = dictDetails.value(forKey: "image") as! String
            courseData.category             = dictDetails.value(forKey: "category") as! String
            courseData.course_image         = imgUrl + imgName
            let featured                    = dictDetails.value(forKey: "featured")as! String
            courseData.is_featured          = featured == "1" ? true : false
//            courseData.course_id            = dictDetails.value(forKey: "id")as! String
            courseData.user_id              = dictDetails.value(forKey: "user_id")as! String
            courseData.firebase_id          = dictDetails.value(forKey: "firebase_reg_id") as! String
            let editVal                     = dictDetails.value(forKey: "is_reviewed")as! String
            courseData.is_reviewed          = editVal == "1" ? true : false
            let arrList :NSArray = dictResult.value(forKey: "review") as! NSArray
            for index in 0 ..< arrList.count
            {
                let dictDetails : NSDictionary = arrList.object(at: index) as! NSDictionary
                let reviewData = ReviewBO()
                let fname                       = dictDetails.value(forKey: "reviewer_fname") as! String
                let lname                       = dictDetails.value(forKey: "reviewer_lname") as! String
                reviewData.user_name            = fname.capitalized + " " + lname.capitalized
                reviewData.rating               = dictDetails.value(forKey: "rating") as! String
                reviewData.user_id              = dictDetails.value(forKey: "user_id")as! String
                reviewData.review_title         = dictDetails.value(forKey: "title")as! String
                reviewData.review_id            = dictDetails.value(forKey: "id")as! String
                reviewData.reviewDesc           = dictDetails.value(forKey: "description") as! String
                reviewData.date                 = dictDetails.value(forKey: "date") as! String
                let imgName:String = dictDetails.value(forKey: "profile_pic") as! String
                reviewData.user_pic             = imgUrl + imgName
                courseData.arrReviews.add(reviewData)
            }
        }
        completeBlock(status!, "", courseData)
    }
    
    
    
    // MARK: -
    // MARK: - Update Course Service
    class func callUpdateCourseService(_ dictPram: NSDictionary, _isFromPic: Bool, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        if _isFromPic
        {
            BaseParser.callMultiPartDataWebService(serverUrl: APP_SERVICE(str: "Course/update/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
                if isSuccess
                {
                    print(dictResult)
                    let status = dictResult.value(forKey: "status") as? Bool
                    let strMsg = dictResult.value(forKey: "message") as? String
                    completeBlock(status!, strMsg!)
                }
                else{
                    completeBlock(isSuccess, "Something Went Wrong")
                }
            }
        }
        else{
            BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Course/update/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
                if isSuccess
                {
                    let status = dictResult.value(forKey: "status") as? Bool
                    let strMsg = dictResult.value(forKey: "message") as? String
                    completeBlock(status!, strMsg!)
                }
                else{
                    completeBlock(isSuccess, "Something Went Wrong")
                }
            }
        }
    }
    // MARK: -
    // MARK: - Product List Service
    class func callProductListService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList : NSArray) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Product/list") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                MarketParser.parseProductList(dictResult, complete: { (status, strMsg, arrList) in
                    completeBlock(status, strMsg, arrList)
                })
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", [])
            }
        }
    }
    
    class func parseProductList(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let arrProductList: NSMutableArray?
        arrProductList = []
        if status!{
            let arrList :NSArray = dictResult.value(forKey: "product_list") as! NSArray
            
            for index in 0 ..< arrList.count
            {
                let dictDetails : NSDictionary = arrList.object(at: index) as! NSDictionary
                let productData = ProductDetailsBO()
                let activity                   = dictDetails.value(forKey: "product_nm") as! String
                productData.name               = activity.capitalized
                //image
                let imgName:String = dictDetails.value(forKey: "profile_pic") as! String
                productData.user_pic           = imgUrl + imgName
                let arrImg = dictDetails.value(forKey: "product_images") as! NSArray
                if arrImg.count != 0
                {
                    for i in 0..<arrImg.count
                    {
                        let dictVal : NSDictionary = arrImg.object(at: i) as! NSDictionary
                        let imgData = ImageBO()
                        let img = dictVal.value(forKey: "image") as! String
                        imgData.image_name  = imgUrl + img
                        imgData.image_id    = dictVal.value(forKey: "id") as! String
                        productData.arrImages.add(imgData)
                    }
                }
                productData.user_email         = dictDetails.value(forKey: "email") as! String
                productData.product_type       = dictDetails.value(forKey: "category") as! String
                let fname                      = dictDetails.value(forKey: "fname") as! String
                let lname                      = dictDetails.value(forKey: "lname") as! String
                productData.user_name          = fname.capitalized + " " + lname.capitalized
                productData.product_id         = dictDetails.value(forKey: "id")as! String
                let editVal                    = dictDetails.value(forKey: "is_editable")as! String
                productData.is_editable        = editVal == "1" ? true : false
                productData.user_loc           = dictDetails.value(forKey: "location")as! String
                productData.user_id            = dictDetails.value(forKey: "user_id")as! String
                productData.price              = dictDetails.value(forKey: "price")as! String
                let flagged                    = dictDetails.value(forKey: "is_flagged")as! String
                productData.is_flagged         = flagged == "1" ? true : false
                let featured                   = dictDetails.value(forKey: "featured")as! String
                productData.is_featured        = featured == "1" ? true : false
                productData.firebase_id        = dictDetails.value(forKey: "firebase_id") as! String
                arrProductList?.add(productData)
            }
        }
        completeBlock(status!, "", arrProductList!)
    }
    
    // MARK: -
    // MARK: - Service List Service
    class func callServiceListService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList : NSArray) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Service/list") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                MarketParser.parseServiceList(dictResult, complete: { (status, strMsg, arrList) in
                    completeBlock(status, strMsg, arrList)
                })
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", [])
            }
        }
    }
    class func parseServiceList(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let arrServiceList: NSMutableArray?
        arrServiceList = []
        if status!{
            let arrList :NSArray = dictResult.value(forKey: "service_list") as! NSArray
            
            for index in 0 ..< arrList.count
            {
                let dictDetails : NSDictionary = arrList.object(at: index) as! NSDictionary
                let serviceData = ServiceDetailsBO()
                let imgName:String = dictDetails.value(forKey: "profile_pic") as! String
                serviceData.user_pic           = imgUrl + imgName
                let imgCharName:String = dictDetails.value(forKey: "Picture") as! String
                serviceData.service_image      = imgUrl + imgCharName
                serviceData.user_email         = dictDetails.value(forKey: "email") as! String
                serviceData.service_type       = dictDetails.value(forKey: "Service_type") as! String
                let fname                      = dictDetails.value(forKey: "fname") as! String
                let lname                      = dictDetails.value(forKey: "lname") as! String
                serviceData.user_name          = fname.capitalized + " " + lname.capitalized
                serviceData.service_id         = dictDetails.value(forKey: "Id")as! String
                serviceData.service_desc       = dictDetails.value(forKey: "Description")as! String
                let editVal                    = dictDetails.value(forKey: "is_editable")as! String
                serviceData.is_editable        = editVal == "1" ? true : false
                serviceData.user_loc           = dictDetails.value(forKey: "Location_address")as! String
                serviceData.user_id            = dictDetails.value(forKey: "User_id")as! String
                serviceData.user_Phone         = dictDetails.value(forKey: "phone")as! String
                serviceData.rating             = dictDetails.value(forKey: "rating")as! String
                let flagged                    = dictDetails.value(forKey: "is_flagged")as! String
                serviceData.is_flagged         = flagged == "1" ? true : false
                serviceData.firebase_id        = dictDetails.value(forKey: "firebase_reg") as! String
                let hide_details                      = dictDetails.value(forKey: "hide_details")as? String != nil ? dictDetails.value(forKey: "hide_details")as! String : ""
                serviceData.is_hidden          = hide_details == "1" ? true : false
                
                arrServiceList?.add(serviceData)
            }
        }
        completeBlock(status!, "", arrServiceList!)
    }

    // MARK: -
    // MARK: - Create Service WebService
    class func callCreateServiceWebService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ strId :String) -> Void) {
        BaseParser.callMultiPartDataWebService(serverUrl: APP_SERVICE(str: "Service/create/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                var strId = ""
                if status!
                {
                    strId = (dictResult.value(forKey: "id") as? String)!
                }
                
                completeBlock(status!, strMsg!, strId)            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", "")
            }
        }
    }
    
    // MARK: -
    // MARK: - Service Details webService
    class func callServiceDetailsWebService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ objList : ServiceDetailsBO) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Service/detail") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                MarketParser.parseServiceDetails(dictResult, complete: { (status, strMsg, objValue) in
                    completeBlock(status, strMsg, objValue)
                })
            }
            else{
                let objval = ServiceDetailsBO()
                completeBlock(isSuccess, "Something Went Wrong", objval)
            }
        }
    }
    
    class func parseServiceDetails(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ objDetails: ServiceDetailsBO) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let serviceData = ServiceDetailsBO()
        if status!{
            let dictDetails : NSDictionary = dictResult.value(forKey: "details") as! NSDictionary
            
            let activity                     = dictDetails.value(forKey: "service_type") as! String
            serviceData.service_type         = activity.capitalized
            serviceData.service_desc         = dictDetails.value(forKey: "description") as! String
            serviceData.user_email           = dictDetails.value(forKey: "email") as! String
            serviceData.user_loc             = dictDetails.value(forKey: "location")as! String
            serviceData.rating               = String(dictDetails.value(forKey: "rating") as! Int)
            let imgName:String               = dictDetails.value(forKey: "image") as! String
            serviceData.service_id           = dictDetails.value(forKey: "id") as! String
            serviceData.service_image        = imgUrl + imgName
            serviceData.user_id              = dictDetails.value(forKey: "user_id")as! String
            let fname                        = dictDetails.value(forKey: "fname") as! String
            let lname                        = dictDetails.value(forKey: "lname") as! String
            serviceData.user_name            = fname.capitalized + " " + lname.capitalized
            serviceData.user_Phone           = dictDetails.value(forKey: "phone") as! String
            let editVal                      = dictDetails.value(forKey: "is_reviewed")as! String
            serviceData.firebase_id          = dictDetails.value(forKey: "firebase_reg") as! String
            serviceData.is_reviewed          = editVal == "1" ? true : false
            
            let hide_details                      = dictDetails.value(forKey: "hide_details")as? String != nil ? dictDetails.value(forKey: "hide_details")as! String : ""
            serviceData.is_hidden          = hide_details == "1" ? true : false
            
            let arrList :NSArray = dictResult.value(forKey: "review") as! NSArray
            for index in 0 ..< arrList.count
            {
                let dictDetails : NSDictionary  = arrList.object(at: index) as! NSDictionary
                let reviewData = ReviewBO()
                let fname                       = dictDetails.value(forKey: "reviewer_fname") as! String
                let lname                       = dictDetails.value(forKey: "reviewer_lname") as! String
                reviewData.user_name            = fname.capitalized + " " + lname.capitalized
                reviewData.rating               = dictDetails.value(forKey: "rating") as! String
                reviewData.user_id              = dictDetails.value(forKey: "user_id")as! String
                reviewData.review_title         = dictDetails.value(forKey: "title")as! String
                reviewData.review_id            = dictDetails.value(forKey: "id")as! String
                reviewData.reviewDesc           = dictDetails.value(forKey: "description") as! String
                reviewData.date                 = dictDetails.value(forKey: "date") as! String
                let imgName:String              = dictDetails.value(forKey: "profile_pic") as! String
                reviewData.user_pic             = imgUrl + imgName
                serviceData.arrReviews.add(reviewData)
            }
        }
        completeBlock(status!, "", serviceData)
    }

    // MARK: -
    // MARK: - Update Service webService
    class func callUpdateServiceWebService(_ dictPram: NSDictionary, _isFromPic: Bool, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        if _isFromPic
        {
            BaseParser.callMultiPartDataWebService(serverUrl: APP_SERVICE(str: "Service/update/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
                if isSuccess
                {
                    print(dictResult)
                    let status = dictResult.value(forKey: "status") as? Bool
                    let strMsg = dictResult.value(forKey: "message") as? String
                    completeBlock(status!, strMsg!)
                }
                else{
                    completeBlock(isSuccess, "Something Went Wrong")
                }
            }
        }
        else{
            BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Service/update/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
                if isSuccess
                {
                    let status = dictResult.value(forKey: "status") as? Bool
                    let strMsg = dictResult.value(forKey: "message") as? String
                    completeBlock(status!, strMsg!)
                }
                else{
                    completeBlock(isSuccess, "Something Went Wrong")
                }
            }
        }
    }
    
    // MARK: -
    // MARK: - Create Product WebService
    class func callCreateProductWebService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ strId :String) -> Void) {
        BaseParser.callMultiPartDataWebService(serverUrl: APP_SERVICE(str: "Product/create/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                var strId = ""
                if status!
                {
                    strId = (dictResult.value(forKey: "id") as? String)!
                }
                
                completeBlock(status!, strMsg!, strId)            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", "")
            }
        }
    }
    // MARK: -
    // MARK: - Update Product webService
    class func callUpdateProductWebService(_ dictPram: NSDictionary, _ isWithPic: Bool, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        if isWithPic
        {
            BaseParser.callMultiPartDataWebService(serverUrl: APP_SERVICE(str: "Product/update/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
                if isSuccess
                {
                    print(dictResult)
                    let status = dictResult.value(forKey: "status") as? Bool
                    let strMsg = dictResult.value(forKey: "message") as? String
                    completeBlock(status!, strMsg!)
                }
                else{
                    completeBlock(isSuccess, "Something Went Wrong")
                }
            }        }
        else{
            BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Product/update/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
                if isSuccess
                {
                    let status = dictResult.value(forKey: "status") as? Bool
                    let strMsg = dictResult.value(forKey: "message") as? String
                    completeBlock(status!, strMsg!)
                }
                else{
                    completeBlock(isSuccess, "Something Went Wrong")
                }
            }
        }
    }
    // MARK: -
    // MARK: - Product Details webService
    class func callProductDetailsWebService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ objList : ProductDetailsBO) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Product/detail") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                MarketParser.parseProductDetails(dictResult, complete: { (status, strMsg, objValue) in
                    completeBlock(status, strMsg, objValue)
                })
            }
            else{
                let objval = ProductDetailsBO()
                completeBlock(isSuccess, "Something Went Wrong", objval)
            }
        }
    }
    class func parseProductDetails(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ objDetails: ProductDetailsBO) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let productData = ProductDetailsBO()
        if status!{
            let dictDetails : NSDictionary = dictResult.value(forKey: "details") as! NSDictionary
            
            let activity                     = dictDetails.value(forKey: "category") as! String
            productData.product_type         = activity.capitalized
            productData.product_desc         = dictDetails.value(forKey: "description") as! String
            productData.user_email           = dictDetails.value(forKey: "email") as! String
            productData.user_loc             = dictDetails.value(forKey: "location")as! String
            productData.name                 = dictDetails.value(forKey: "name") as! String
            productData.rating               = dictDetails.value(forKey: "rating") as! Int
            productData.price                = dictDetails.value(forKey: "price") as! String
            productData.firebase_id          = dictDetails.value(forKey: "firebase_reg") as! String
            
            let hide_details                      = dictDetails.value(forKey: "hide_details")as? String != nil ? dictDetails.value(forKey: "hide_details")as! String : ""
            productData.is_hidden          = hide_details == "1" ? true : false
            
            let arrImg = dictDetails.value(forKey: "product_images") as! NSArray
            for i in 0..<arrImg.count
            {
                let dictVal : NSDictionary = arrImg.object(at: i) as! NSDictionary
                let imgData = ImageBO()
                let img = dictVal.value(forKey: "image") as! String
                imgData.image_name  = imgUrl + img
                imgData.image_id    = dictVal.value(forKey: "id") as! String
                productData.arrImages.add(imgData)
            }


            productData.user_id              = dictDetails.value(forKey: "user_id")as! String
            let fname                        = dictDetails.value(forKey: "fname") as! String
            let lname                        = dictDetails.value(forKey: "lname") as! String
            productData.user_name            = fname.capitalized + " " + lname.capitalized
            productData.user_Phone           = dictDetails.value(forKey: "phone") as! String
            let editVal                      = dictDetails.value(forKey: "is_reviewed")as! String
            productData.is_reviewed          = editVal == "1" ? true : false
            let featuredVal                  = dictDetails.value(forKey: "featured")as! String
            productData.is_featured          = featuredVal == "1" ? true : false

            let arrList :NSArray = dictResult.value(forKey: "review") as! NSArray
            for index in 0 ..< arrList.count
            {
                let dictDetails : NSDictionary  = arrList.object(at: index) as! NSDictionary
                let reviewData = ReviewBO()
                let fname                       = dictDetails.value(forKey: "reviewer_fname") as! String
                let lname                       = dictDetails.value(forKey: "reviewer_lname") as! String
                reviewData.user_name            = fname.capitalized + " " + lname.capitalized
                reviewData.rating               = dictDetails.value(forKey: "rating") as! String
                reviewData.user_id              = dictDetails.value(forKey: "user_id")as! String
                reviewData.review_title         = dictDetails.value(forKey: "title")as! String
                reviewData.review_id            = dictDetails.value(forKey: "id")as! String
                reviewData.reviewDesc           = dictDetails.value(forKey: "description") as! String
                reviewData.date                 = dictDetails.value(forKey: "date") as! String
                let imgName:String              = dictDetails.value(forKey: "profile_pic") as! String
                reviewData.user_pic             = imgUrl + imgName
                productData.arrReviews.add(reviewData)
            }
        }
        completeBlock(status!, "", productData)
    }

    // MARK: -
    // MARK: - Delete Product Image WebService
    class func callDeleteProductImageWebService(_ strId: String, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Product/delete_image") as! String, postValues: ["id" : strId], methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                completeBlock(status!, strMsg!)
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong")
            }
        }
    }



    
}
