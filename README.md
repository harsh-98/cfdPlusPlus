# CFD++ Idea

## Landslide prediction

### Problem identification

Natural disasters like landslides, avalanches, floods and debris flows can result in enormous property damage and human casualties in mountainous regions. Therefore, efforts to measure and to monitor potential landslides are essential to ensure human safety and to protect civil infrastructure. To observe the behavior of slopes, monitoring systems have been installed or manual inspections by human experts have been conducted. Land Slide monitoring is based on geotechnical instrumentations using. However, cablebased systems are costly, require continuous maintenance. To overcome these limitations, wireless sensor networks and Internet of things are a viable alternative technology. 

The diagram shows what the causes of landslide are and how it affects to the different level of ground. These are explained below.
![](a.png)

Factors Influencing Landslide Risk --
- Slope
- bedrock
- Surface water
- Earthquakes

### Solution
The landslide monitoring system automatically calculates the inverse velocity, and determines whether and when landslides can be expected. The system is composed of two subsystems, a wireless sensor network and a server system.

In each node various sensors are used. Such as:
- Accelerometer sensor
- Soil moisture sensor
- Temperature sensor
- Vibration sensor

The data send by these devices will be used by azure iot hub and edge platform for detecting sudden rise. If sensors cross the threshold value, alert system is activated. Alert system is in different form. Big buzzers are activated in the land slide area. There is an advanced alert system using SMS, email and IoT application. Azure maps will highlight the affected areas.

For deploying the system we would be working under one of our Professor Satessh kumar Pedoju.

## Reference
- http://ieeexplore.ieee.org/iel7/7361/4427201/07488208.pdf?arnumber=7488208
- http://troindia.in/journal/ijcesr/vol5iss4part4/20-23.pdf
- https://www.onlinejournal.in/IJIRV3I4/164.pdf
- https://pdfs.semanticscholar.org/5ef0/fd9b608159fc4fe637e84379a60694dbd5f1.pdf
