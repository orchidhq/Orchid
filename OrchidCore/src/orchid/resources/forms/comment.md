---
form: 
  title: 'Leave us a comment'
  attributes:
    data-netlify: true
  fields:
    name:
      label: 'Name (optional)'
      type: text
      span: 'auto'
      order: 1
    occupation:
      label: 'Occupation'
      type: 'dropdown'
      required: true
      span: 'auto'
      order: 3
      options:
        student: Student
        programmer: Programmer
        teacher: Teacher
        pastor: Pastor
        other: Other
    comments:
      label: 'Comments'
      type: 'textarea'
      required: true
      span: 'full'
      order: 4
---

Comment final page
