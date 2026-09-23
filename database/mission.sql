DROP TABLE IF EXISTS Persona;

CREATE TABLE IF NOT EXISTS Persona (
    Name VARCHAR NOT NULL,
    Age INT,
    Gender VARCHAR,
    Ethnicity VARCHAR,
    Profession VARCHAR,
    Background VARCHAR,
    Needs VARCHAR,
    Goals VARCHAR,
    Skills VARCHAR,
    Image VARCHAR,
    PRIMARY KEY (Name)
);

INSERT INTO Persona(Name, Age, Gender, Ethnicity, Profession, Background, Needs, Goals, Skills, Image)
VALUES (
    "Dr. Claire Thompson",
    45,
    "Female",
    "Afro French",
    "Senior Climate Scientist at the Australian Climate Research Institute",
    "Dr. Claire has been a climate scientist for the past 20 years. She has expertise in atmospheric science and ecological impact analysis across Australia, with frequent collaborations with scientists based in the US, Greenland, and Antarctic research team.",
    "Clean, simplistic landing displays for findings, with appropriate data visualization|Option to expand data into tabular form for detailed analysis|Options to download presented data|Smooth UI for mobile devices to share with policymakers/corporations|Interactive maps/charts presenting data",
    "Access findings from long-term research in a visual, and credible format|Educate the public and policymakers about climate change",
    "Able to understand tabular data|Comfortable with using filters, sorting and downloading information|Can easily access websites with straightforward navigation|Comfortable with universal standards of UI/UX in website development",
    "personas/dr_claire_thompson.png"
);

INSERT INTO Persona(Name, Age, Gender, Ethnicity, Profession, Background, Needs, Goals, Skills, Image)
VALUES (
    "Richard Winton",
    64,
    "Male",
    "Caucasian",
    "Politician specialising in eliminating climate change",
    "Richard always loved nature and the outdoors. He is upset with the direction the world is going in, because of which he became a politician, dedicated to reversing the damage that his generation and the generations before him did to the planets climate.",
    "Clear and easy to use interface|A way to analyse trends in weather patterns over certain time periods|The ability to export the data in a printable format for easy sharing in political environments",
    "Create plans and solutions to climate issues|Get the government to invest resources into the climate battle",
    "Has limited understanding of modern UI/UX standards|Has basic knowledge of the internet|Capable of reading and taking in large amounts of information at once",
    "personas/richard_winton.png"
);

INSERT INTO Persona(Name, Age, Gender, Ethnicity, Profession, Background, Needs, Goals, Skills, Image)
VALUES (
    "Tyler Grant",
    24,
    "Male",
    "Caucasian",
    "Is an avid surfer and life-guard, and also works as a travel guide for tourists coming to the beach",
    "Tyler has always been an enthusiastic surfer and lover of the ocean. However recently Tyler has become increasingly worried for the health of marine life due to climate change and the threat of increased ocean temperatures and extreme weather events.",
    "Easy to understand page layout|Concise and readable information on specific weather data|The ability to share data through links to social media",
    "To share reliable data to help spread awareness and push for stronger climate action|To better understand the impact climate change is having on the oceans, and how weather events might affect marine ecosystems",
    "Has grown up using technology and is proficient navigating websites that use typical design patterns|Prefers to view short-form content and would require easy access to particular topics of interest|Can feel overwhelmed by large amounts of data displayed at once|Has minimal knowledge of the impact of certain weather events",
    "personas/tyler_grant.png"
);

DROP TABLE IF EXISTS GroupMember;

CREATE TABLE IF NOT EXISTS GroupMember (
    Name VARCHAR NOT NULL,
    StudentNum VARCHAR,
    SubTask VARCHAR,
    PRIMARY KEY (Name)
);

INSERT INTO GroupMember(Name, StudentNum, SubTask)
VALUES (
    "Azhaanul Haque Farooqui",
    "Removed for privacy",
    "A"
);

INSERT INTO GroupMember(Name, StudentNum, SubTask)
VALUES (
    "Sam Virgona",
    "Removed for privacy",
    "B"
);

INSERT INTO GroupMember(Name, StudentNum, SubTask)
VALUES (
    "Kieran Collins",
    "Removed for privacy",
    "C"
);
SELECT * FROM GroupMember;