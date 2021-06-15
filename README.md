# Parking Android Application

## Project Requirements and Implementation:
Create an Android application for the Parking App with the following functionalities:

### User Profile
Your app must provide appropriate interface to sign-up, sign-in, sign-out, update profile and delete account. Consider requesting name, email, password, contact number and car plate number from user when they create their account.
If you are inclined and interested in adding profile picture functionality, you may add that in your app. However, it is not the official requirement of the project. No additional grades will be awarded or deducted for including (or not including) this functionality.


### Add Parking
The add parking facility should allow the user to create a new parking record with the following information.
- Building code (exactly 5 alphanumeric characters)
- No. of hours intended to park
- Car License Plate Number (min 2, max 8 alphanumeric characters)
- Suit no. of host (min 2, max 5 alphanumeric characters)
- Parking location (street address, lat and lng)
- date and time of parking (use system date/time)
You should allow the user to input the parking location in two ways:
- enter street name [In this case the app should obtain location coordinates using geocoding]
- use current location of the device [In this case the app should use reverse geocoding to obtain street address]

After accepting and verifying all information, all parking information must be saved to database. You must use either CoreData or Cloud Firestore to save the records. When adding the parking information in the database, make sure that you associate the record with the currently logged in user.


### View Parking
This facility will allow the user to view the list of all the parking they have made. You should display the list with most recent parking first. You should also display the detail view of each parking when user taps on any item of the list. When displaying detail view, display all the information about the parking in appropriate format. In the detail view of parking, allow the user to open the parking location on map and display the route to the parking location from the current location of the device.


## PROJECT :
The below details about the progress in the project are specified in the ReadMe file of the branch (SignIn)
- Screenshots of the UI screens designed
- ER-Diagram
- Google firebase plist files & DB details

### Sign IN Page is designed with the below options
- User Name & Password field validation
- User Name & Password is saved in the database using FireBase
- When login is success, Navigation to the next page
- Create account button navigates to the account creation page

### Create Account Page :
- All required fields created (Name, Email, Password, Phone No, Car No)
- All field validation is done
- On successful account creation, the data is saved to the FireBase under the collection "Account Details"
- Also the username & password is created under the collection "Login Details"

### Add Parking Screen :
- Requests all the required details from the user
- A date picker is diplayed only when the date field is clicked
- A pop up/ dialog box appers when the "Enter Location details" is clicked, which will then have options to enter the lat, log or street name"

### Edit Parking :
- User can edit the parking details
- User can update parking details

### Parking List Screen :
- User can see the list of parked vehicles
- User can see individual details
- User can see the Route either on same screen or on Map application


# Screenshots

## Firebase Screenshot :
![image](https://user-images.githubusercontent.com/80915452/119736632-a4dbab00-be9b-11eb-9b58-6dc6fd97b022.png)

## App Screenshots :
### Sign In -
![Sign In](https://user-images.githubusercontent.com/80915452/122118868-30ff4380-ce46-11eb-85f0-bc7e99f4bac3.png)

### Create Profile -
![Create Account](https://user-images.githubusercontent.com/80915452/122119009-57bd7a00-ce46-11eb-9894-393b0783fba6.png)

### Update Profile -
![Update Profile](https://user-images.githubusercontent.com/80915452/122119028-5f7d1e80-ce46-11eb-8b6a-2135b3479518.png)

### Parking List -
![Parking List](https://user-images.githubusercontent.com/80915452/122119058-66a42c80-ce46-11eb-86ea-22975e3e9d17.png)

### Parking Details -
![Parking Details](https://user-images.githubusercontent.com/80915452/122119095-6efc6780-ce46-11eb-8f6d-5bbfea2187fa.png)

### Menu Items -
![Menu Items](https://user-images.githubusercontent.com/80915452/122119137-7885cf80-ce46-11eb-8b4e-227923aaae1d.png)

### Add Parking -
![Add Parking](https://user-images.githubusercontent.com/80915452/122119165-7facdd80-ce46-11eb-8a34-85803de2359b.png)

### Update Parking -
![Update Parking](https://user-images.githubusercontent.com/80915452/122124359-cd2c4900-ce4c-11eb-8686-0c307ed02e16.png)


### Sign Out -
![Signed Out](https://user-images.githubusercontent.com/80915452/122118897-38265180-ce46-11eb-94ba-ded8d20a2e57.png)


## ER-Diagram :
![Parking Android ED](https://user-images.githubusercontent.com/80915452/122117652-bda90200-ce44-11eb-9491-ecb54b5acc94.png)


# Contributors 🌱
@itzgannu
<img src="https://user-images.githubusercontent.com/80915452/122121186-f34fea00-ce48-11eb-8c4e-edbc4274f1f2.jpg" width="100" height="100">
@SDivya287
<img src="https://user-images.githubusercontent.com/80915452/122122502-8178a000-ce4a-11eb-9562-66d3e7516a51.JPG" width="79" height="100">


