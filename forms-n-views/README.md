Forms 'n' Views
===============

Forms 'n' Views is intended to be something of a "Designer Lite" for the web - a tool for making
quick changes to legacy design elements without loading Designer.

Currently, it can be used for basic Form, View, Folder, and Style Sheet modification and (save
forms, for now) creation. In the cases of Forms, Views, and Folders, the editor UI is geared
towards working with the data structure and not the UI - in this sense, it is useful as a support
tool for an XPage-focused database, where the appearance of the design elements in the classic
client isn't important (though it could be used to modify existing elements without
destroying the appearance).

All design-element manipulation is done by manipulating the DXL representations of the notes. This
means that existing information is generally preserved, barring any corrupting edge cases.

Forms 'n' Views requires Domino 8.5.3 and the XPages Extension Library or Upgrade Pack 1.