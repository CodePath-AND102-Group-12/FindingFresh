# Finding Fresh - Milestone #3

## Table of Contents

1. [Overview](https://github.com/CodePath-AND102-Group-12/FindingFresh/edit/master/README.md#overview)
1. [Product Spec](https://github.com/CodePath-AND102-Group-12/FindingFresh/edit/master/README.md#product-spec)
1. [Wireframes & Prototype](https://github.com/CodePath-AND102-Group-12/FindingFresh/edit/master/README.md#wireframes--prototype)
1. [Milestone #3 Build Progress](https://github.com/CodePath-AND102-Group-12/FindingFresh/edit/master/README.md#milestone-3-build-progress)
1. [Demo Day Video](https://github.com/CodePath-AND102-Group-12/FindingFresh/edit/master/README.md#demo-day-video)

## Overview

### Description

An app to find local Farmer's Markets, their vendors, and how to reach them.

### App Evaluation

- **Category:** Shopping/ event listing app
- **Mobile:** Maps and location services can  find nearby farmer's markets. These features make the mobile format ideal for accessing information via mobile device rather than a website.
- **Story:** The app will increase access to farmer's markets for shoppers and vendors through location services, product category sorting and a shopping list. It will be helpful for a wide range of people trying to access farmer's markets.
- **Market:** The userbase of this service will comprise of people or organizations interested in listing their farmers market for prospective customers. This app will also allow users to look into farmers markets in their location.
- **Habit:** The app will become a weekly or daily habit for avid farmer's market attendees and the usage will be expanded beyond farmer's market shopping days through use of the shopping list.
- **Scope:** Shoppers and vendors can access information about location and categories of items provided. The shopping list function will allow shoppers to utilize the app even on days they are not attending farmer's markets.

## Product Spec

### 1. User Features (Required and Optional)

**Required Features**

- [x] Recyler View for markets
- [x] Bottom Navigation Bar
- [x] Firebase to store market information
- [x] Google Maps API Integration to navigate user to local events or show on map
- [x] Account and login capabilities
- [x] User settings
- [x] Shopping/Grocery list
- [x] Calendar sort
- [x] List of user's favorite farmers markets
- [x] Map showing nearby markets
- [x] Users can add markets

### 2. Screen Archetypes

#### Login Activity
- Login Activity
    - As User


#### Main Activity
- Farmers Market Listing
    - sort by date (list)
    - sort by distance (map)
- Farmers Market Map (User)
- Calendar
    - Upcoming events
    - Events by pin

#### Alternate Activities
- Farmers Market Detail
    - Cover/hero image
    - Address - tap into Google Maps
    - Schedule
    - Contact information (email/phone)
    - List of vendors
- Settings
    - Log out
- Add a Farmers Market
- Search bar

### 3. Navigation

**Tab Navigation** (Tab to Screen) (Bottom Navigation)

* Scroll Market Listing
* Visual Map List
* Calendar

**Flow Navigation** (Screen to Screen)

- Login
    - Create Account
    - Login
- Market Listing
    - List details of different events
    - Click into particular event details
        - Click into particular vendor details
        - Navigate to event
- Map View
- Shopping List
    - Add/remove items
- Settings

## Wireframes & Prototype

### Initial wireframe
developed during Unit 7 session
<img src="https://github.com/CodePath-AND102-Group-12/Temp/blob/master/Group%2012%20Initial%20Wireframe.png?raw=true" width="600">

### Refined Digital Wireframes & Mockups
Refined wireframe after the Unit 7 session
<img src="https://github.com/CodePath-AND102-Group-12/Temp/blob/master/Group%2012%20Final%20Digital%20Wireframe.png?raw=true" width="600">

[Also viewable on Figma](https://www.figma.com/community/file/1166378987628110358)

### Interactive Prototype
<img src="https://github.com/CodePath-AND102-Group-12/Temp/blob/master/Group%2012%20Interactice%20Prototype.gif?raw=true" height="600">

[Recorded with Kap for MacOS](https://getkap.co/)

## Milestone 3 Build Progress
![Video Walkthrough](https://github.com/CodePath-AND102-Group-12/FindingFresh/blob/master/Milestone_3.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Demo Day Video

[![Demo Day Video hosted on Youtube](http://img.youtube.com/vi/YyflcZbPNuc/0.jpg)](http://www.youtube.com/watch?v=YyflcZbPNuc "Finding Fresh - CodePath Demo")
