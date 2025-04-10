**Mobile Development 2024/25 Portfolio**
# API Description

Student ID: `c23035992`

In developing my Android application, the Android API provided a multitude of features for me to be able build my app according to it's functional and non-functional requirements.

ConstraintLayouts were used to build UI interfaces with elements that link to one another. I used ConstraintLayouts over FrameLayouts as Android Studio have developed the GUI designer with ConstraintLayouts in mind, streamlining the GUI design phase instead of tediously digging through XML code to create my designs.

To house my data, I opted to use the Room Persistance Library. The use of annotations, DAOs and data entities felt very logical to me and made handling database operations much simpler than I anticipated. 

One of the core features of my app was displaying data from the database onto the UI using RecyclerViews. I designed a row layout in my XML, and used an adapter for my RecyclerView to display database data on the UI whilst still keeping a clean look. The RecyclerViews I built also dynamically updated by using the LiveData class in my getMeals DAO function, making it easy to then use .observe(viewLifecycleOwner) to check for changes in the LiveData and then update accordingly in the RecyclerView

For entering dates and times, I used the DatePickerDialog and TimePickerDialog popups to show either a calendar or clock for easy input into date/time fields. This could have been done just via a regular edit text, but the user may incorrectly enter the date format so handling input through a PickerDialog and then formatting the text which would be entered is a much more convienient option.

I also used the ActivityResultCaller interface and utilising the .PickMultipleVisualMedia() method to allow the user to input multiple photos that are on their phone to the app gallery.


