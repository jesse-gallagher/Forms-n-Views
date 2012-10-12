Todo
====

- Switch to Apache's BASE64 implementation
- Session persistence (i.e. keep open tabs open on page reload)
- Tab uniqueness
- Tab close order
- Esc to close tab
- Handle the icon when a DB is on the same server but not set to allow URL access
- Handle when the server isn't found
- Make client-side formula validation work
- Make the whole thing not look like crap
- Write a readme and home page text
- Error handling for when a database is unavailable (deletion, server down, ACL change, etc.)
- Figure out why, at least for forms, sometimes a second save just doesn't work, while the first and third do
	- It may be related to keypress or validation events - clicking the buttom while still in a field refreshes but fails, while clicking outside first works
- Forms
	- Filter out subforms (?)
- Views
	- Categorization
	- Totals
	- Handle shared columns?
- To add:
	- Subforms (?)
	- Pages (?)
	- File Resources
	- Images
	- Shared Fields
	- Shared Columns (?)