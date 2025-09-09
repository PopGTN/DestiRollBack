# RollBack

This is project that will back up the player's inventory and xp on there death or when they join the game or when they
quit. This Date can be stored locally or in database. This can be configured in the config file. Furthermore, all
messages will be configurable so that it can be modified for any language or format.

## Project Requirments

* ``/rollback [player]`` - This is command will open a gui menu that will display the players
  inventories' back up. For each backup there will be item representing it in the gui menu. These items can one of the 3
  following 3 items Skeleton Head, Oak Door, and barrier block. Each block is configurable and it represents the type of
  event that cause the inventory to be backup. The types of events can be ``JOIN`` which is represented by The Oak Door amd ``QUIT`` which is represented by the barrier Block 
  and ``DEATH`` is represented by the Skeleton Head. When hovering over a item that represents a inventory backup it will display the date & time of
  the backup the items'.
* All messages must be configurable
* All the blocks That Represent a save Event type must be configurable
* the hover in the menu for DEATH should show the time and the reason of the death 

