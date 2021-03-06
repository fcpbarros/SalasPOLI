# SalasPOLI

This app was developed in 2019 with the goal of making the coordination sector of the University of Pernambuco able to control and display information about classrooms and which professor is at a given classroom at a given point in time.

This app (**SalasPOLI**) complements the app **alunopoli** (located in another repo) 

**Features**
- Firebase Realtime Database
- Recycler View
- Adapters
- Linear Layout
- Constraint Layout
- Relative Layout
- Search bar
- QR code scanner

The App starts with the following screen

![screenshot](images/SalasPOLI.jpeg)

Then, in order to proper register the information, the user of this app has to open the QR code scanner and scan the the classroom key and the professor key and such information
will be sent to an online **Database** (Google Firebase) so all the students can easily see such information on the student version of the app (**alunospoli**).

The **scanner** is opened by clicking on the scan button as shown above.

It is also possible to see classroom information by clicking the **Salas** button as shown below

![screenshot](images/screenSalas.jpeg)

And visualize the notices (**Avisos**) by clicking **Avisos** button

![screenshot](images/screenAvisos.jpeg)

