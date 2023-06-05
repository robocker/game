# Intro #

Robocker is a game running by docker.  It's aim is to gives flexibility for players by changing part of the game.
Robocker is inspired by 'Z' game- fighting tanks, capturing flags etc.

# Installation #

Currently game is tested on Windows 10(home), 11(home) and Ubuntu.

It's possible to run

## WSL2 ##

  * Installing wsl2:
    * https://learn.microsoft.com/pl-pl/windows/wsl/install
    * or: https://www.youtube.com/watch?v=F9Rg3CCuaEo
  * Installing docker for windows: https://docs.docker.com/desktop/install/windows-install

More advanced example how to play with it:
https://www.youtube.com/watch?v=A0eqZujVfYU



## Run Robocker ##

To run game you need to build docker images of projects: engine, tankBasic and player. ~~Project folder is mounted in /vagrant folder To go there in terminal you can just type `cd /vagrant`~~ (only for Virtualbox approach). To make it by wsl2 just open terminal (powershell/command prompt) in folder where you clonned robocker (eg. C:/projects/robocker).

Details about creating particular images you can find in projecs READMEs: [engine](./engine/README.md), [player](./player/README.md), [tankBasic](./tankBasic/README.md), [tankPython](./tankPython/README.md).


## Virtualbox- old aproach- not recommended ##
Home editons of Windows not support docker natively so it's neccesary to run Docker in Virtual Machine: https://www.virtualbox.org/
Oracle VM VirtualBox Extension Pack
https://www.oracle.com/virtualization/technologies/vm/downloads/virtualbox-downloads.html#extpack

## Run Virutal Machine ##

Tip: Before creating Virtual Machine you can open VirtualBox setting (file/global setting) and check if default folder is for you ok.

You can create you virtual machine mannually or use Vagrant (https://www.vagrantup.com/). After second option you only need tu run in comandline:

```
## In main folder of Robocker
vagrant up
```
After finish you can log in you can just run:
```
vagrant ssh
```

Tips after running (commands inside Ubuntu):

* You can set password of vagrant user by: `sudo passwd vagrant` (notice that any '*' won't be display during typing)
* It could be convienient to install desktop environment e.g. gnome:
 ```
 sudo apt-get update
 sudo apt-get install ubuntu-gnome-desktop
 ```
  (see: https://www.wikihow.com/Install-Gnome-on-Ubuntu ). After reboot you can click 'show' in VirtualBox UI.
* Full screen (https://askubuntu.com/questions/310320/cant-get-fullscreen-resolution-on-ubuntu-hosted-on-virtualbox#310323):
```
sudo apt-get update
sudo apt-get install virtualbox-guest-dkms
sudo dpkg-reconfigure virtualbox-guest-dkms
```
* Enabling sharing clipboard: https://scribles.net/sharing-files-and-clipboard-between-host-and-guest-in-virtualbox/