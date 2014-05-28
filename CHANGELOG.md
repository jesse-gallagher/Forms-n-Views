Changelog
=========

- 2012/12/1
	- Added the ability to create new forms
	- Added the ability to modify view/folder column titles
- 2012/11/12
	- Fixed a bug when saving stylesheets (for some reason, it sometimes added extra CR characters to the BASE64 output)
- 2012/10/12
	- Added the ability to create new views and folders
- 2012/10/3
	- Added the ability to add and remove form fields
- 2012/10/2
	- Added the ability to create new stylesheets
	- Added the Codemirror editor for CSS editing
- 2012/9/10
	- Switched from the "soria" Dojo theme to "claro"
	- Added default/computed formula editing for form fields
- 2012/9/8
	- Cleaned up the "preview" XML representation at the bottom of each editor
- 2012/9/7
	- Added formula and item name validation for forms, folders, and views
	- Added "move up" and "move down" actions for view and folder columns and form fields
	- Added proper clearing of the "dirty" flag on save
- 2012/9/4
	- Switched from an alert on successful save to a dijit.Toaster
- 2012/8/30
	- Filtered out folders from the Views list
	- Added folders as an independent type