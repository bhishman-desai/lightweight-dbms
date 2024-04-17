# lightweight-dbms
This is a custom lightweight database management system which can be used for simple query selection and retrieval

## Project Breakdown

The database management system is divided into three key processes:

- Parsing: This is where we handle the parsing of the query and validate its syntax. It's all about making sure the
  query is well-formed and ready to go.
- Executing: Next up is the execution phase. Here, we bifurcate the query based on its type and make the appropriate
  call to the database.
- Fetching: Finally, we have the fetching process. This is where we retrieve the data from the database (in this case,
  files) and deliver it to the end user.

## Supported Commands

The application supports the following DDL and DML commands:
```sql
-- CREATE TABLE
create table <table_name> (<column_name>, <column_name>);

-- CREATE USER
create user <username> identified by <password>;
       
-- SELECT 
select * from <table_name>;
-- or 
select <column_name> from <table_name>; 
-- or 
select <column_name> from <table_name> where <condition>;

-- INSERT
insert into <table_name> values (value, value), (value, value), (value, value);

```

## Contribution

Contributions to enhance the app's functionality or address any issues are welcome. Feel free to use the provided source code as a reference for creating similar applications for your institution.

## License

This project is licensed under the [MIT License](LICENSE.md).