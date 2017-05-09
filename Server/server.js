var express = require('express');
var bodyParser = require("body-parser");
var Pool = require('pg').Pool;
var crypto = require("crypto");
var app = express();


var hashing = {
  iterations  : 10000,
  outputLen : 512,
  hashFunc  : 'sha512'
};

var config = {
  host: 'localhost',
  user: 'root',
  database: 'Trackr',
  port: '5432',
  password: 'toor'
};

var pool = new Pool(config);

app.use(bodyParser.json());
var session = require('express-session');

app.use(session({
  secret: 'secretValue',
  cookie: {maxAge: 1000*60*60*24*30}
}));

app.get('/test-db',function(req,res){
    pool.query('SELECT * FROM test',function(err,result){
       if (err){
           res.status(500).send(err.toString());
       } else{
           res.send(JSON.stringify(result.rows));
       }
    });
});

function hash(input,salt){
  var hashed_string = crypto.pbkdf2Sync(input,salt,hashing.iterations,hashing.outputLen,hashing.hashFunc);
  return hashed_string.toString('hex');
}

app.post("/login",function(req,res){
  var username = req.body.username;
  var password = req.body.password;
  pool.query('SELECT * FROM "users" WHERE username = $1',[username],function(err,result){
    if (err) {
      res.status(500).send(err.toString())
    }
    else {
    	if(result.rows.length === 0) {
    		res.status(403).send("Invalid username or password");
    	}
    	else
    	{
    		var db_pass = result.rows[0].password;
    		var user_pass = hash(password,username);
    		if(user_pass === db_pass) {
          req.session.auth = {userId: result.rows[0].user_id};
          console.log();
          res.send("Credentials Valid");
    		}
    		else {
    			res.status(403).send("Invalid username or password");
    		}
    	}
    }
  });
});

app.get('/check-login',function(req,res) {
  if(req.session && req.session.auth && req.session.auth.userId) {
    res.send("You are logged in as: " + req.session.auth.userId.toString());
  }
  else {
    res.send("You are not logged in");
  }
});

app.get('/logout',function(req,res) {
  delete req.session.auth;
  res.send('Logged out');
});

app.post("/register",function(req,res){
  var fame = req.body.fname;
  var lname = req.body.lname;
  var phone = req.body.phone;
  var password = req.body.password;
  var db_pass = hash(password,username);
  pool.query('INSERT INTO "users" (fname,lname,phone,password) VALUES ($1,$2,$3,$4)',[fname,lname,phone,db_pass],function(err,result){
    if(err){
      res.status(500).send(err.toString());
    }
    else{
      // res.send("User succesfully registered");
      res.send(JSON.stringify(result.rows));
    }
  });
});

var port = 8080; // Use 8080 for local development because you might already have apache running on 80
app.listen(8080, function () {
  console.log(`Trackr server listening on port ${port}!`);
});





// var morgan = require('morgan');
// var path = require('path');
//var http = require('http');
// app.use(morgan('combined'));

// app.use(morgan('combined'));
// app.use(express.static("public"));
// app.use('/ui',express.static(__dirname+'/ui'));

// app.get("/hash/:input",function(req,res){
//   hashedString = hash(req.params.input,"salt");
//   res.send(hashedString);
// });
//

