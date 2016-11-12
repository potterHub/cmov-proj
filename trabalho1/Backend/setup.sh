#!/usr/bin/env bash

cd "${0%/*}"

if type sqlite3 > /dev/null; then
  sqlite="sqlite3"
elif type sqlite > /dev/null; then
  sqlite="sqlite"
fi

if [[ -z "$sqlite" ]]; then
  1>&2 echo "Please install sqlite3"
  exit 1
fi

server_dir="server/"
db_dir="sqlite/"
db_file="$db_dir/app.sqlite3"
create_file="$db_dir/create.sql"
insert_file="$db_dir/insert.sql"

rm -f "$db_file"
echo "Creating database"
"$sqlite" "$db_file" < "$create_file"
echo "Inserting data to database"
"$sqlite" "$db_file" < "$insert_file"

cd "$server_dir"

server_pid="$(pgrep server)"

if [[ -n "$server_pid" ]]; then
  kill -KILL "$server_pid"
  while kill -0 "$server_pid" 2>/dev/null; do sleep 1; done
fi

if [[ ! -f  "./bin/server" ]]; then
  echo "Getting libraries"
  gb vendor restore
  echo "Building server"
  gb build server
fi
echo "Running server"
./bin/server
