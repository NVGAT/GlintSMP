# Glint SMP plugin
### What is it?
The Glint SMP plugin is used in the Glint SMP (shocker) and its main use is to make gameplay more exciting on said SMP.
### What does it do?
The Glint SMP plugin aims to improve the staleness of a regular Minecraft SMP by making players drop enchanted books on death. 
When a player dies, they will drop an enchanted book with a random enchantment and a random enchantment leve between one and ten.
This can result in enchantments such as Sharpness X, but can also result in enchantments such as Aqua Affinity VII. It's very hit-or-miss, 
and is mostly either very good, mid or very bad.
### How does it work?
The Glint SMP plugin utilizes the Spigot API to ensure maximum compatibility with any modern Minecraft server software. 
Its native version is Minecraft 1.19, so anything below or above that isn't guaranteed to work.
### How do I install this?
#### DISCLAIMER: The plugin's native Minecraft version is SpigotMC version 1.19. ANY OTHER SETUP (other server softwares or other versions) may still work due to compatibility layers, but it's recommended to not use anything downstream from PurpurMC.
Great! Now that you've definitely read the disclaimer, let's proceed.
First and foremost, we will check that we have Java 17 installed. Open up a terminal window and type the following:
```shell
java --version
```
In my case, this is the output. Everything that mentions Java 17 should be fine.
```shell
openjdk 17.0.5 2022-10-18
IBM Semeru Runtime Open Edition 17.0.5.0 (build 17.0.5+8)
Eclipse OpenJ9 VM 17.0.5.0 (build openj9-0.35.0, JRE 17 Windows 10 amd64-64-Bit Compressed References 20221018_298 (JIT enabled, AOT enabled)
OpenJ9   - e04a7f6c1
OMR      - 85a21674f
JCL      - 32d2c409a33 based on jdk-17.0.5+8)
```
In this case, we will be using PurpurMC due to its staggering performance. We will download the .jar file from [PurpurMC](https://www.purpurmc.org).\
Then we will make a directory for your server to sit in. Move the downloaded .jar file and rename it to server.jar\
After you've done that, you should create a start file. They should be named differently for each operating system, so I'll cover the main three:\
`Windows: start.bat`\
`Linux: start.sh`\
`MacOS: buynewpc.dontactuallynameitlikethis`\
The contents of the file should be the same for all of these operating systems:\
```shell
java -jar -Xms512M -Xmx2G server.jar nogui
```
#### Let me explain what this does:
`java` - Tells the computer that we will be using Java\
`-jar` - Tells the Java Runtime Environment (JRE) that it will be running a .jar file\
`-Xms512M` - Tells the JRE that it will always use a minimum of 512 megabytes. You can adjust this value to your liking.\
`-Xmx2G` - Tells the JRE that it can use a maximum of 2 gigabytes for the server. Depending on the size of your server and your computer, you might want to change this value.\
`server.jar` - Points the JRE to the server.jar file\
`nogui` - Tells the server not to display any GUI. You can remove this if you want a GUI
