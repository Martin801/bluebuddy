//
//  UserParser.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/23/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class UserParser: NSObject {
    
    // MARK: -
    // MARK: - Sign In Service
    class func callSignUpService(dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ strOTP: String) -> Void)
    {
        BaseParser.callWebService(serverUrl: APP_SERVICE(str: "Users/create/") as! String, postValues: dictPram, methodName: "POST", isLoader: true) { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                var otp: String
                if status!{
                    let valueOtp = dictResult.value(forKey: "otp") as! Int
                    otp = String(valueOtp)
                    let value = dictResult.value(forKey: "user_id") as? String
                    UserDefaults.standard.set(value, forKey: kUserID)
                }
                else
                {
                    otp = ""
                }
                completeBlock(status!, strMsg!, otp)
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", "")
            }
        }
    }
    // MARK: -
    // MARK: - Activate User Service
    class func callActivateUserService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebService(serverUrl: APP_SERVICE(str: "User/activate/") as! String, postValues: dictPram, methodName: "POST", isLoader: true) { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                if status!
                {
                    let strToken = dictResult.value(forKey: "Token") as? String
                    let dictVal = dictResult.value(forKey: "user_details") as? NSDictionary
                    let strPage = dictVal?.value(forKey: "next_step") as? String
                    UserDefaults.standard.set(strToken, forKey:kUserToken)
                    UserDefaults.standard.set(strPage, forKey:kPageNo)

                }
                completeBlock(status!, "")
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong")
            }
            
        }

    }
    // MARK: -
    // MARK: - Login With Facebook
    class func callLoginWithFacebook(_ dictPram: NSDictionary, isLoader:Bool, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String , _ isNewUser: Bool) -> Void) {
        BaseParser.callWebService(serverUrl: APP_SERVICE(str: "Users/social/") as! String, postValues: dictPram, methodName: "POST", isLoader: true) { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let new_socialuser = dictResult.value(forKey: "new_socialuser") as? Bool
                if new_socialuser!
                {
                    let value = dictResult.value(forKey: "user_id") as? String
                    UserDefaults.standard.set(value, forKey: kUserID)
                }
                if status!
                {
                    let strToken = dictResult.value(forKey: "Token") as? String
                    UserDefaults.standard.set(strToken, forKey:kUserToken)
                    let dictVal = dictResult.value(forKey: "user_details") as? NSDictionary
                    let strPage = dictVal?.value(forKey: "next_step") as? String
                    let strPreviousPage = UserDefaults.standard.value(forKey: kPageNo) as? String
                    let previousPage = strPreviousPage == nil ? 0 : Int(strPreviousPage!)
                    let page = Int(strPage!)
                    if previousPage! > page!
                    {
                        UserDefaults.standard.set(strPreviousPage, forKey:kPageNo)
                        
                    }
                    else{
                        UserDefaults.standard.set(strPage, forKey:kPageNo)
                        
                    }
                }

                completeBlock(status!, "", new_socialuser!)
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", false)
            }
        }
    }
    // MARK: -
    // MARK: - Login In Service
    class func callLoginUserService(_ dictPram: NSDictionary, isLoader:Bool, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String , _ strUserStatus: String, _ dictUserValue: NSDictionary) -> Void) {
        BaseParser.callWebService(serverUrl: APP_SERVICE(str: "User/login/") as! String, postValues: dictPram, methodName: "POST", isLoader: true) { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String

                let user_status = dictResult.value(forKey: "user_status") as? String
                if user_status! == "0"
                {
                    let value = dictResult.value(forKey: "user_id") as? String
                    UserDefaults.standard.set(value, forKey: kUserID)
                }

                if status!
                {
                    let strToken = dictResult.value(forKey: "Token") as? String
                    let dictVal = dictResult.value(forKey: "user_details") as? NSDictionary
                    let strPage = dictVal?.value(forKey: "next_step") as? String
                    UserDefaults.standard.set(strToken, forKey:kUserToken)
                    let strPreviousPage = UserDefaults.standard.value(forKey: kPageNo) as? String
                    let previousPage = strPreviousPage == nil ? 0 : Int(strPreviousPage!)
                    let page = Int(strPage!)
                    if previousPage! > page!
                    {
                        UserDefaults.standard.set(strPreviousPage, forKey:kPageNo)

                    }
                    else{
                        UserDefaults.standard.set(strPage, forKey:kPageNo)

                    }
                }
                completeBlock(status!, strMsg!, "\(user_status!)",dictResult)
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", "2",dictResult)
            }
        }
    }
    
    // MARK: -
    // MARK: - Resend Otp Service
    class func callsendMobileNoForVerificationService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebService(serverUrl: APP_SERVICE(str: "User/resendotp/") as! String, postValues: dictPram, methodName: "POST" , isLoader: true) { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                if status!
                {
                    let strToken = dictResult.value(forKey: "Token") as? String
                    let dictVal = dictResult.value(forKey: "user_details") as? NSDictionary
                    let strPage = dictVal?.value(forKey: "next_step") as? String
                    UserDefaults.standard.set(strToken, forKey:kUserToken)
                    UserDefaults.standard.set(strPage, forKey:kPageNo)
                    
                }
                completeBlock(status!, "\(String(describing: dictResult.value(forKey: "otp") as? Int))!")
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong")
            }
            
        }
    }
    
    
    // MARK: -
    // MARK: - Forgot Password Service
    class func callForgotPasswordService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebService(serverUrl: APP_SERVICE(str: "Users/forgotpassword/") as! String, postValues: dictPram, methodName: "POST", isLoader: true) { (isSuccess, dictResult) in
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
    
    // MARK: -
    // MARK: - Change Password Service
    class func callChangePasswordService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "profile/changepassword") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
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

    
    // MARK: -
    // MARK: - Update Profile Service
    class func callUpdateProfileService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Profile/update/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
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
    
    
    // MARK: -
    // MARK: - Add Certification Service
    class func callUpdateCertificateService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Profile/certification/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
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
    
    
    // MARK: -
    // MARK: - Update Profile for image Service
    class func callUploadProfilePictureService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ dictResult: NSDictionary) -> Void) {
        BaseParser.callMultiPartDataWebService(serverUrl: APP_SERVICE(str: "Profile/update/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
            if isSuccess{
                let status = dictResult.value(forKey: "status") as? Bool
                let strMsg = dictResult.value(forKey: "message") as? String
                completeBlock(status!, strMsg!, dictResult)
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong",dictResult)
            }
        }
    }
    
    class func callUploadFirebaseIDService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callMultiPartDataWebService(serverUrl: APP_SERVICE(str: "Profile/readdfirebasereg/") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
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
    
    // MARK: -
    // MARK: - Profile Details Service
    class func callProfileDetailsService(_ strUserId: String, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ objProfile: ProfileBO) -> Void) {
        if strUserId.isEmpty
        {
            BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Profile/details/") as! String, postValues: [:], methodName: "GET") { (isSuccess, dictResult) in
                if isSuccess{
                    self.parseProfileData(dictResult, complete: { (status, strMsg, objProfile) in
                        DispatchQueue.main.async {
                            completeBlock(status, "", objProfile)
                        }
                    })
                }
                else{
                    let objVal = ProfileBO()
                    completeBlock(isSuccess, "Something Went Wrong", objVal)
                }
            }
        }
        else{
            BaseParser.callWebServiceWithHeaderForGet(serverUrl: APP_SERVICE(str: "User/details") as! String, postValues: ["user_id" : strUserId], methodName: "GET", isLoader:true) { (isSuccess, dictResult) in
                if isSuccess{
                    self.parseUserProfileData(dictResult, complete: { (status, strMsg, objProfile) in
                        DispatchQueue.main.async {
                            completeBlock(status, "", objProfile)
                        }
                    })
                }
                else{
                    let objVal = ProfileBO()
                    completeBlock(isSuccess, "Something Went Wrong", objVal)
                }
            }
        }
    }
    // parse update profile
    class func parseProfileData(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ objProfile: ProfileBO) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let profileData = ProfileBO()
        if status! {
            let dictValue : NSDictionary = dictResult.value(forKey: "profile_details") as! NSDictionary
            print(dictValue)
           
            profileData.first_name = dictValue.value(forKey: "First_name") as! String
            profileData.last_name = dictValue.value(forKey: "Last_name") as! String
            profileData.about = dictValue.value(forKey: "About") as! String
            profileData.email = dictValue.value(forKey: "Email") as! String
            profileData.userId = dictValue.value(forKey: "Id") as! String
            profileData.interested_in = dictValue.value(forKey: "Interested_in") as! String
            profileData.location = dictValue.value(forKey: "Location") as! String
            profileData.lat = dictValue.value(forKey: "Location_lat") as! String
            profileData.long = dictValue.value(forKey: "Location_long") as! String
            profileData.phone = dictValue.value(forKey: "Phone") as! String
            let image = dictValue.value(forKey: "Profile_pic") as! String
            profileData.profile_pic = imgUrl + image
            profileData.social = dictValue.value(forKey: "Social") as! String
            profileData.social_id = dictValue.value(forKey: "Social_id") as! String
            profileData.website_link = dictValue.value(forKey: "Website_link") as! String
            profileData.buddy_counter = dictValue.value(forKey: "buddy_counter") as! String
            profileData.fb_url = dictValue.value(forKey: "fb_url") as! String
            profileData.ggle_url = dictValue.value(forKey: "ggle_url") as! String
            profileData.ingm_url = dictValue.value(forKey: "ingm_url") as! String
            profileData.twtr_url = dictValue.value(forKey: "twtr_url") as! String
//            profileData.firebase_id = dictValue.value(forKey: "Firebase_reg_id") as! String
            if !profileData.fb_url.isEmpty && profileData.fb_url != " "
            {
                profileData.arrSocialLink.add("1")
            }
            else{
                
            }
            if !profileData.twtr_url.isEmpty && profileData.twtr_url != " "
            {
                profileData.arrSocialLink.add("2")
            }
            else{
                
            }
            if !profileData.ggle_url.isEmpty && profileData.ggle_url != " "
            {
                profileData.arrSocialLink.add("3")
            }
            else{
                
            }
            if !profileData.ingm_url.isEmpty && profileData.ingm_url != " "
            {
                profileData.arrSocialLink.add("4")
            }
            else{
                
            }
            let arrList :NSArray = dictResult.value(forKey: "certification_details") as! NSArray
            
            for index in 0 ..< arrList.count
            {
                let dictDetails : NSDictionary = arrList.object(at: index) as! NSDictionary
                let certData = CertificateBO()
                certData.Cert_level             = dictDetails.value(forKey: "Cert_level") as! String
                certData.Cert_level2            = dictDetails.value(forKey: "Cert_level2") as! String
                certData.Cert_level3            = dictDetails.value(forKey: "Cert_level3") as! String
                certData.Cert_name              = dictDetails.value(forKey: "Cert_name") as! String
                certData.Cert_no                = dictDetails.value(forKey: "Cert_no") as! String
                certData.Cert_type              = dictDetails.value(forKey: "Cert_type") as! String
                certData.Cert_id                = dictDetails.value(forKey: "Id") as! String
                
                if !certData.Cert_level.isEmpty
                {
                    certData.arrLevel.add(certData.Cert_level)
                    if !certData.Cert_level2.isEmpty
                    {
                        certData.arrLevel.add(certData.Cert_level2)
                        if !certData.Cert_level3.isEmpty
                        {
                            certData.arrLevel.add(certData.Cert_level3)
                        }

                    }
                }
                profileData.arrCertificate.add(certData)

            }
            let arrPartList :NSArray = dictResult.value(forKey: "my_participation") as! NSArray
            
            for index in 0 ..< arrPartList.count
            {
                let dictDetails : NSDictionary = arrPartList.object(at: index) as! NSDictionary
                let tripData = TripsDetailsBO()
                let activity                = dictDetails.value(forKey: "activity") as! String
                tripData.activity           = activity.capitalized
                tripData.event_date         = dictDetails.value(forKey: "event_date") as! String
                tripData.event_id           = dictDetails.value(forKey: "event_id")as! String
                tripData.is_editable        = false
                tripData.event_loc          = dictDetails.value(forKey: "location")as! String
                tripData.no_of_participant  = dictDetails.value(forKey: "participants")as! String
                tripData.user_id            = dictDetails.value(forKey: "user_id")as! String
                tripData.trip_desc          = dictDetails.value(forKey: "description") as! String
                profileData.arrParticipation.add(tripData)
                
            }
            let arrTripList :NSArray = dictResult.value(forKey: "trip_details") as! NSArray
            
            for index in 0 ..< arrTripList.count
            {
                let dictDetails : NSDictionary = arrTripList.object(at: index) as! NSDictionary
                let tripData = TripsDetailsBO()
                let activity                = dictDetails.value(forKey: "activity") as! String
                tripData.activity           = activity.capitalized
                tripData.event_date         = dictDetails.value(forKey: "event_date") as! String
                tripData.event_date1         = dictDetails.value(forKey: "original_event_date") as! String
                tripData.event_id           = dictDetails.value(forKey: "event_id")as! String
                tripData.is_editable        = true
                tripData.event_loc          = dictDetails.value(forKey: "location")as! String
                tripData.no_of_participant  = dictDetails.value(forKey: "participants")as! String
                tripData.user_id            = dictDetails.value(forKey: "user_id")as! String
                tripData.trip_desc          = dictDetails.value(forKey: "description") as! String
                profileData.arrTrips.add(tripData)
                
            }


            LoginConstants.sharedManager.objProfile = profileData
            print(LOGIN_CONSTANT.objProfile.first_name)

        }
        completeBlock(status!, "", profileData)

    }
    
    // parse update profile
    class func parseUserProfileData(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ objProfile: ProfileBO) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let profileData = ProfileBO()
        if status! {
            let dictValue : NSDictionary = dictResult.value(forKey: "details") as! NSDictionary
            print(dictValue)
            
            profileData.first_name = dictValue.value(forKey: "First_name") as! String
            profileData.last_name = dictValue.value(forKey: "Last_name") as! String
            profileData.about = dictValue.value(forKey: "About") as! String
            profileData.email = dictValue.value(forKey: "Email") as! String
            profileData.userId = dictValue.value(forKey: "Id") as! String
            profileData.interested_in = dictValue.value(forKey: "Interested_in") as! String
            profileData.location = dictValue.value(forKey: "Location") as! String
            profileData.lat = dictValue.value(forKey: "Location_lat") as! String
            profileData.long = dictValue.value(forKey: "Location_long") as! String
            profileData.phone = dictValue.value(forKey: "Phone") as! String
            let image = dictValue.value(forKey: "Profile_pic") as! String
            profileData.profile_pic = imgUrl + image
            profileData.social = dictValue.value(forKey: "Social") as! String
            profileData.social_id = dictValue.value(forKey: "Social_id") as! String
            profileData.website_link = dictValue.value(forKey: "Website_link") as! String
            profileData.buddy_counter = dictValue.value(forKey: "buddy_counter") as! String
            profileData.firebase_id = dictValue.value(forKey: "Firebase_reg_id") as! String
            profileData.fb_url = dictValue.value(forKey: "fb_url") as! String
            profileData.ggle_url = dictValue.value(forKey: "ggle_url") as! String
            profileData.ingm_url = dictValue.value(forKey: "ingm_url") as! String
            profileData.twtr_url = dictValue.value(forKey: "twtr_url") as! String
            let buddy = dictResult.value(forKey: "is_buddy") as! String
            profileData.isBuddy = buddy == "Add" ? "3" : buddy
            if !profileData.fb_url.isEmpty && profileData.fb_url != " "
            {
                profileData.arrSocialLink.add("1")
            }
            else{
                
            }
            if !profileData.twtr_url.isEmpty && profileData.twtr_url != " "
            {
                profileData.arrSocialLink.add("2")
            }
            else{
                
            }
            if !profileData.ggle_url.isEmpty && profileData.ggle_url != " "
            {
                profileData.arrSocialLink.add("3")
            }
            else{
                
            }
            if !profileData.ingm_url.isEmpty && profileData.ingm_url != " "
            {
                profileData.arrSocialLink.add("4")
            }
            else{
                
            }
            let arrList :NSArray = dictResult.value(forKey: "certification_details") as! NSArray
            
            for index in 0 ..< arrList.count
            {
                let dictDetails : NSDictionary = arrList.object(at: index) as! NSDictionary
                let certData = CertificateBO()
                certData.Cert_level             = dictDetails.value(forKey: "Cert_level") as! String
                certData.Cert_level2            = dictDetails.value(forKey: "Cert_level2") as! String
                certData.Cert_level3            = dictDetails.value(forKey: "Cert_level3") as! String
                certData.Cert_name              = dictDetails.value(forKey: "Cert_name") as! String
                certData.Cert_no                = dictDetails.value(forKey: "Cert_no") as! String
                certData.Cert_type              = dictDetails.value(forKey: "Cert_type") as! String
                certData.Cert_id                = dictDetails.value(forKey: "Id") as! String
                
                if !certData.Cert_level.isEmpty
                {
                    certData.arrLevel.add(certData.Cert_level)
                    if !certData.Cert_level2.isEmpty
                    {
                        certData.arrLevel.add(certData.Cert_level2)
                        if !certData.Cert_level3.isEmpty
                        {
                            certData.arrLevel.add(certData.Cert_level3)
                        }
                        
                    }
                }
                profileData.arrCertificate.add(certData)
                
            }
            let arrTripList :NSArray = dictResult.value(forKey: "trip_details") as! NSArray
            
            for index in 0 ..< arrTripList.count
            {
                let dictDetails : NSDictionary = arrTripList.object(at: index) as! NSDictionary
                let tripData = TripsDetailsBO()
                let activity                = dictDetails.value(forKey: "activity") as! String
                tripData.activity           = activity.capitalized
                tripData.event_date         = dictDetails.value(forKey: "event_date") as! String
                tripData.event_id           = dictDetails.value(forKey: "event_id")as! String
                tripData.is_editable        = dictDetails.value(forKey: "user_id") as! String == LOGIN_CONSTANT.objProfile.userId ? true:false
                tripData.event_loc          = dictDetails.value(forKey: "location")as! String
                tripData.no_of_participant  = dictDetails.value(forKey: "participants")as! String
                tripData.user_id            = dictDetails.value(forKey: "user_id")as! String
                tripData.trip_desc          = dictDetails.value(forKey: "description") as! String
                let partVal                 = dictDetails.value(forKey: "participated")as! String
                tripData.is_participated    = partVal == "1" ? true : false
                profileData.arrTrips.add(tripData)
                
            }
        }
        completeBlock(status!, "", profileData)
        
    }

    
    // MARK: -
    // MARK: - Add Buddy Service
    class func callAddBuddyService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Buddy/create") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
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
    
    // MARK: -
    // MARK: - My Listing Service
    class func callMyListingService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Profile/mylist") as! String, postValues: [:], methodName: "GET") { (isSuccess, dictResult) in
            if isSuccess{
                self.parseMyListing(dictResult, complete: { (status, strMsg, arrList) in
                    DispatchQueue.main.async {
                        completeBlock(status, "", arrList)
                    }
                })
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", [])
            }
        }
    }
    
    class func parseMyListing(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let arrMyList: NSMutableArray?
        arrMyList = []
        if status!{
            let arrList :NSArray = dictResult.value(forKey: "charter_list") as! NSArray
            if arrList.count != 0
            {
                let listingData = MyListingBO()
                listingData.section_Name = "CHARTER/BOAT RENTAL"
                listingData.type = 1
                for index in 0 ..< arrList.count
                {
                    let dictDetails : NSDictionary = arrList.object(at: index) as! NSDictionary
                    let charterData = CharterDetailsBO()
                    let activity                   = dictDetails.value(forKey: "charter_nm") as! String
                    charterData.name               = activity.capitalized
                    let imgCharName:String         = dictDetails.value(forKey: "image") as! String
                    charterData.charter_image      = imgUrl + imgCharName
                    let fname                      = dictDetails.value(forKey: "fname") as! String
                    let lname                      = dictDetails.value(forKey: "lname") as! String
                    charterData.user_name          = fname.capitalized + " " + lname.capitalized
                    charterData.charter_id         = dictDetails.value(forKey: "id")as! String
                    let editVal                    = dictDetails.value(forKey: "is_editable")as! String
                    charterData.is_editable        = editVal == "1" ? true : false
                    charterData.user_loc           = dictDetails.value(forKey: "location")as! String
                    charterData.user_id            = dictDetails.value(forKey: "user_id")as! String
                    charterData.price              = dictDetails.value(forKey: "price")as! String
                    let featured                   = dictDetails.value(forKey: "featured")as! String
                    charterData.is_featured        = featured == "1" ? true : false
                    
                    listingData.arrValue.add(charterData)
                }
                arrMyList?.add(listingData)
            }
            
            let arrList1 :NSArray = dictResult.value(forKey: "course_list") as! NSArray
            if arrList1.count != 0
            {

                let listingData2 = MyListingBO()
                listingData2.section_Name = "COURSE"
                listingData2.type = 1
                for index in 0 ..< arrList1.count
                {
                    let dictDetails : NSDictionary = arrList1.object(at: index) as! NSDictionary
                    let courseData = CoursesDetailsBO()
                    let imgCharName:String        = dictDetails.value(forKey: "image") as! String
                    courseData.course_image       = imgUrl + imgCharName
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
                    let featured                  = dictDetails.value(forKey: "featured")as! String
                    courseData.is_featured        = featured == "1" ? true : false
                    
                    listingData2.arrValue.add(courseData)
                }
                arrMyList?.add(listingData2)
            }
            
            let arrList2 :NSArray = dictResult.value(forKey: "product_list") as! NSArray
            if arrList2.count != 0
            {

            let listingData3 = MyListingBO()
            listingData3.section_Name = "PRODUCT"
            listingData3.type = 1

                for index in 0 ..< arrList2.count
                {
                    let dictDetails : NSDictionary = arrList2.object(at: index) as! NSDictionary
                    let productData = ProductDetailsBO()
                    productData.name                = dictDetails.value(forKey: "product_nm") as! String
                    let imgCharName:String          = dictDetails.value(forKey: "image") as! String
                    var arrImg = imgCharName.components(separatedBy: ",")
                    for i in 0..<arrImg.count
                    {
                        productData.arrImages.add(imgUrl + arrImg[i])
                    }
                    let fname                       = dictDetails.value(forKey: "fname") as! String
                    let lname                       = dictDetails.value(forKey: "lname") as! String
                    productData.user_name           = fname.capitalized + " " + lname.capitalized
                    productData.product_id          = dictDetails.value(forKey: "id")as! String
                    let editVal                     = dictDetails.value(forKey: "is_editable")as! String
                    productData.is_editable         = editVal == "1" ? true : false
                    productData.user_loc            = dictDetails.value(forKey: "location")as! String
                    productData.user_id             = dictDetails.value(forKey: "user_id")as! String
                    productData.price               = dictDetails.value(forKey: "price")as! String
                    let featured                    = dictDetails.value(forKey: "featured")as! String
                    productData.is_featured         = featured == "1" ? true : false
                    
                    listingData3.arrValue.add(productData)
                }
                arrMyList?.add(listingData3)
            }
            
            let arrList3 :NSArray = dictResult.value(forKey: "service_list") as! NSArray
            if arrList3.count != 0
            {

                let listingData4 = MyListingBO()
                listingData4.section_Name = "SERVICE"
                listingData4.type = 2
                for index in 0 ..< arrList3.count
                {
                    let dictDetails : NSDictionary = arrList3.object(at: index) as! NSDictionary
                    let serviceData = ServiceDetailsBO()
                    let imgCharName:String          = dictDetails.value(forKey: "Picture") as! String
                    serviceData.service_image       = imgUrl + imgCharName
                    serviceData.user_email          = dictDetails.value(forKey: "email") as! String
                    serviceData.service_type        = dictDetails.value(forKey: "Service_type") as! String
                    let fname                       = dictDetails.value(forKey: "fname") as! String
                    let lname                       = dictDetails.value(forKey: "lname") as! String
                    serviceData.user_name           = fname.capitalized + " " + lname.capitalized
                    serviceData.service_id          = dictDetails.value(forKey: "Id")as! String
                    serviceData.service_desc        = dictDetails.value(forKey: "Description")as! String
                    let editVal                     = dictDetails.value(forKey: "is_editable")as! String
                    serviceData.is_editable         = editVal == "1" ? true : false
                    serviceData.user_loc            = dictDetails.value(forKey: "Location_address")as! String
                    serviceData.user_lat            = dictDetails.value(forKey: "Location_lat")as! String
                    serviceData.user_long           = dictDetails.value(forKey: "Location_long")as! String
                    serviceData.user_id             = dictDetails.value(forKey: "User_id")as! String
                    serviceData.user_Phone          = dictDetails.value(forKey: "phone")as! String
                    serviceData.rating              = dictDetails.value(forKey: "rating")as! String
                    
                    listingData4.arrValue.add(serviceData)
                }
                arrMyList?.add(listingData4)
            }
        }
        completeBlock(status!, "", arrMyList!)
    }
    
    // MARK: -
    // MARK: - My Review Service
    class func callMyReviewService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Review/myreview") as! String, postValues: [:], methodName: "GET") { (isSuccess, dictResult) in
            if isSuccess{
                    self.parseMyReviewList(dictResult, complete: { (status, strMsg, arrList) in
                        DispatchQueue.main.async {
                            completeBlock(status, "", arrList)
                        }
                    })
            }
            else
            {
                completeBlock(isSuccess, "Something Went Wrong", [])
            }
        }
    }
    
    class func parseMyReviewList(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let arrReviewList: NSMutableArray?
        arrReviewList = []
        if status!{
            let arrList :NSArray = dictResult.value(forKey: "details") as! NSArray
            for index in 0 ..< arrList.count
            {
                let dictDetails : NSDictionary = arrList.object(at: index) as! NSDictionary
                let reviewData = ReviewBO()
                let fname                       = dictDetails.value(forKey: "fname") as! String
                let lname                       = dictDetails.value(forKey: "lname") as! String
                reviewData.user_name            = fname.capitalized + " " + lname.capitalized
                reviewData.rating               = dictDetails.value(forKey: "rating") as! String
                reviewData.review_title         = dictDetails.value(forKey: "title")as! String
                reviewData.reviewDesc           = dictDetails.value(forKey: "description") as! String
                reviewData.date                 = dictDetails.value(forKey: "review_dt") as! String
                reviewData.for_id               = dictDetails.value(forKey: "For_id") as! String
                reviewData.review_id            = dictDetails.value(forKey: "review_id") as!String
                let arrVal :NSArray             = dictDetails.value(forKey: "fordetails") as! NSArray
                let dictValue : NSDictionary    = arrVal.object(at: 0) as! NSDictionary
                reviewData.item_id              = dictValue.value(forKey: "Id") as! String
                let revData                     = dictDetails.value(forKey: "Rev_for") as! String
                reviewData.review_for           = revData
                if revData == "product"
                {
                    reviewData.itemName         = dictValue.value(forKey: "Pr_name") as! String
                }
                else if revData == "charter"{
                    reviewData.itemName         = dictValue.value(forKey: "Ch_name") as! String
                }
                else if revData == "course"{
                    reviewData.itemName         = dictValue.value(forKey: "Agency_name") as! String
                }
                else if revData == "service"{
                    reviewData.itemName         = dictValue.value(forKey: "Service_type") as! String
                }
                arrReviewList?.add(reviewData)
            }
        }
        completeBlock(status!, "", arrReviewList!)
    }

    // MARK: -
    // MARK: - My Notification Service
    class func callMyNotificationService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "profile/notification") as! String, postValues: [:], methodName: "GET") { (isSuccess, dictResult) in
            if isSuccess{
                self.parseNotificationList(dictResult, complete: { (status, strMsg, arrList) in
                    DispatchQueue.main.async {
                        completeBlock(status, "", arrList)
                    }
                })
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", [])
            }
        }
    }
    class func parseNotificationList(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let arrNotificationList: NSMutableArray?
        arrNotificationList = []
        if status!{
            let arrList :NSArray = dictResult.value(forKey: "notification_details") as! NSArray
            for index in 0 ..< arrList.count
            {
                let dictDetails : NSDictionary = arrList.object(at: index) as! NSDictionary
                let notiData = NotificationBO()
                notiData.first_name             = dictDetails.value(forKey: "First_name") as! String
                notiData.last_name              = dictDetails.value(forKey: "Last_name") as! String
                notiData.activity_type          = dictDetails.value(forKey: "Activity_type") as! String
                notiData.for_id                 = dictDetails.value(forKey: "For_id")as! String
                notiData.notification_id        = dictDetails.value(forKey: "Id") as! String
                notiData.message                = dictDetails.value(forKey: "Message") as! String
                notiData.notify_for             = dictDetails.value(forKey: "Notify_for") as! String
                let imgUser:String              = dictDetails.value(forKey: "Profile_pic") as! String
                notiData.profile_pic            = imgUrl + imgUser
                notiData.receiver_id            = dictDetails.value(forKey: "Receiver_id") as! String
                notiData.create_date            = dictDetails.value(forKey: "create_dt") as! String
                notiData.requestor_id           = dictDetails.value(forKey: "Requestor_id") as! String
                notiData.user_id                = dictDetails.value(forKey: "User_id") as! String
                notiData.is_accepted            = dictDetails.value(forKey: "is_accepted") as! String
                notiData.is_seen                = dictDetails.value(forKey: "Is_seen") as! String
                notiData.viewed_at              = dictDetails.value(forKey: "Viewed_at") as! String
                arrNotificationList?.add(notiData)
            }
        }
        completeBlock(status!, "", arrNotificationList!)
    }

    // MARK: -
    // MARK: - Accept Buddy Service
    class func callAcceptBuddyService(_ dictPram: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void) {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Buddy/accept") as! String, postValues: dictPram, methodName: "POST") { (isSuccess, dictResult) in
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
    
    // MARK: -
    // MARK: - BuddyList Service
    class func callBuddyListService(_ strUserId: String, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSMutableArray) -> Void)
    {
        BaseParser.callWebServiceWithHeader(serverUrl: APP_SERVICE(str: "Buddy/list") as! String, postValues: [:], methodName: "GET") { (isSuccess, dictResult) in
            if isSuccess{
                UserParser.parseBuddyList(dictResult, complete: { (status, strMsg, arrList) in
                    completeBlock(status, strMsg, arrList as! NSMutableArray)
                })
            }
            else{
                completeBlock(isSuccess, "Something Went Wrong", [])
            }
        }
    }
    class func parseBuddyList(_ dictResult: NSDictionary, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String, _ arrList: NSArray) -> Void)
    {
        let status = dictResult.value(forKey: "status") as? Bool
        let arrPartList: NSMutableArray?
        arrPartList = []
        if status!{
            let arrList :NSArray = dictResult.value(forKey: "details") as! NSArray
            
            for index in 0 ..< arrList.count
            {
                let dictDetails : NSDictionary = arrList.object(at: index) as! NSDictionary
                let userData = ProfileBO()
                userData.email              = dictDetails.value(forKey: "email") as! String
                let userPic = dictDetails.value(forKey: "dp") as! String
                userData.profile_pic        = imgUrl + userPic
                userData.first_name         = dictDetails.value(forKey: "fname") as! String
                userData.last_name          = dictDetails.value(forKey: "lname") as! String
                userData.location           = dictDetails.value(forKey: "location")as! String
                userData.lat                = dictDetails.value(forKey: "latitude")as! String
                userData.userId             = dictDetails.value(forKey: "user_id")as! String
                userData.long               = dictDetails.value(forKey: "longitude")as! String
                arrPartList?.add(userData)
            }
        }
        completeBlock(status!, "", arrPartList!)
    }

    
    // MARK: -
    // MARK: - Viewed Notification Service
    class func callViewedNotificationService(_ strNotiId: String, complete completeBlock: @escaping (_ status: Bool, _ strMsg: String) -> Void)
    {
        BaseParser.callWebServiceWithHeaderAndLoader(serverUrl: APP_SERVICE(str: "Profile/notificationviewed") as! String, postValues: [:], methodName: "POST", isLoader: true) { (isSuccess, dictResult) in
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
