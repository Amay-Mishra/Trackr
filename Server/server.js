var express = require('express');
var morgan = require('morgan');
var path = require('path');
var app = express();
var crypto = require("crypto");
var bodyParser = require("body-parser");
var Pool = require('pg').Pool;
var session = require('express-session');
//var http = require('http');
app.use(morgan('combined'));
app.use(bodyParser.json());
app.use(session({
  secret: 'secretValue',
  cookie: {maxAge: 1000*60*60*24*30}
}));

app.use(morgan('combined'));
app.use(express.static("public"));
app.use('/ui',express.static(__dirname+'/ui'));


var hashing = {
  iterations  : 10000,
  outputLen : 512,
  hashFunc  : 'sha512'
};

var config = {
  //host: 'localhost',
  user: 'amay-mishra',
  database: 'amay-mishra',
  host: 'db.imad.hasura-app.io',
  port: '5432',
  password: process.env.DB_PASSWORD //process.env.DB-PASSWORD,
};

var pool = new Pool(config);
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



function profileTemplating(data) {
  var fname,lname,username,email,description;
  if(data.fname===null)
    fname="";
  else
    fname=data.fname;
  if(data.lname===null)
    lname="";
  else
    lname=data.lname;
  username=data.username;
  email=data.email;
  if(data.description===null)
    description="";
  else
    description=data.description;
  var html = `<link href="/ui/css/bootstrap.css" rel="stylesheet" />
  <script src="/ui/js/jquery.min.js"></script>
  <script src="/ui/js/update-profile.js"></script>
  <script src="/ui/js/bootstrap.min.js"></script>
  <div class="container">
      <h1>Edit Profile</h1>
    	<hr>
  	<div class="row">
        <!-- left column -->
        <div class="col-md-3">
          <div class="text-center">
            <img src="//placehold.it/100" class="avatar img-circle" alt="avatar">
            <h6>Upload a different photo...</h6>
            <input type="file" class="form-control">
          </div>
        </div>
        <!-- edit form column -->
        <div class="col-md-9 personal-info">
          <div class="alert alert-info alert-dismissable" id="alert-box">
            <a class="panel-close close" data-dismiss="alert">×</a>
            <i class="fa fa-coffee"></i>
            This is an <strong>.alert</strong>. Use this to show important messages to the user.
          </div>
          <h3>Personal info</h3>
          <form class="form-horizontal" role="form">
            <div class="form-group">
              <label class="col-lg-3 control-label">First name:</label>
              <div class="col-lg-8">
                <input id="fname" class="form-control" type="text" value="${fname}">
              </div>
            </div>
            <div class="form-group">
              <label class="col-lg-3 control-label">Last name:</label>
              <div class="col-lg-8">
                <input id="lname" class="form-control" type="text" value="${lname}">
              </div>
            </div>
            <div class="form-group">
              <label class="col-lg-3 control-label">Company:</label>
              <div class="col-lg-8">
                <input class="form-control" type="text" value="">
              </div>
            </div>
            <div class="form-group">
              <label class="col-lg-3 control-label">Email:</label>
              <div class="col-lg-8">
                <input id="email" class="form-control" type="text" value="${email}">
              </div>
            </div>
            <div class="form-group">
              <label class="col-lg-3 control-label">Gender:</label>
              <div class="col-lg-8">
                <div class="ui-select">
                  <select id="gender" class="form-control">
                  <option value="secret">I'd like to keep it a secret</option>
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                  </select>
                </div>
              </div>
            </div>
            <div class="form-group">
              <label class="col-md-3 control-label">Username:</label>
              <div class="col-md-8">
                <input class="form-control" type="text" value="${username}">
              </div>
            </div>
            <div class="form-group">
              <label class="col-md-3 control-label">Description:</label>
              <div class="col-md-8">
                <input id="description" class="form-control" type="text" value="${description}">
              </div>
            </div>
            <div class="form-group">
              <label class="col-md-3 control-label">Password:</label>
              <div class="col-md-8">
                <input class="form-control" type="password" value="11111122333">
              </div>
            </div>
            <div class="form-group">
              <label class="col-md-3 control-label">Confirm password:</label>
              <div class="col-md-8">
                <input class="form-control" type="password" value="11111122333">
              </div>
            </div>
            <div class="form-group">
              <label class="col-md-3 control-label"></label>
              <div class="col-md-8">
                <input type="button" onclick="update_profile()" class="btn btn-primary" value="Save Changes">
                <span></span>
                <input type="reset" class="btn btn-default" value="Cancel">
              </div>
            </div>
          </form>
        </div>
    </div>
  </div>
  <hr>`;
  return html;
};



app.get('/profile',function(req,res){
  if(req.session && req.session.auth && req.session.auth.userId) {
    pool.query('SELECT * FROM "users" WHERE user_id = $1',[req.session.auth.userId],function(err,result){
      if (err) {
        res.status(500).send(err.toString())
      }
      else {
        if(result.rows.length === 0) {
          res.status(403).send("Invalid username or password");
        }
        else
        {
          console.log(result.rows[0]);
          res.send(profileTemplating(result.rows[0]));
        }
      }
    });
  }
});

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
  var username = req.body.username;
  var password = req.body.password;
  var email = req.body.email;
  var db_pass = hash(password,username);
  pool.query('INSERT INTO "users" (username,password,email) VALUES ($1,$2,$3)',[username,db_pass,email],function(err,result){
    if(err){
      res.status(500).send(err.toString());
    }
    else{
      // res.send("User succesfully registered");
      res.send(JSON.stringify(result.rows));
    }
  });
});

app.get("/hash/:input",function(req,res){
  hashedString = hash(req.params.input,"salt");
  res.send(hashedString);
});

app.post("/update-profile",function(req,res){
  pool.query('UPDATE "users" SET email=$1,fname=$2,lname=$3,description=$4,gender=$5 WHERE user_id=$6',[req.body.email,req.body.fname,req.body.lname,req.body.description,req.body.gender,req.session.auth.userId],function(err,result){
    if(err){
      res.status(500).send(err.toString());
    }
    else{
      // res.send("User succesfully registered");
      res.status(200).send("Changes succesfully saved");
    }
  });
});


app.get('/',function(req,res){res.sendFile(path.join(__dirname,'ui','home.html'))});

app.get("/article/:articleNo",function(req,res){
  var articleNumber = req.params.articleNo;
  if(articles[articleNumber]){
    res.send(templating(articles[articleNumber]));
  }
  else {
    res.send("Article not found");
  }
});

// app.get('/about',function(req,res){
//     res.send(path.join(__dirname,"ui","html","about.html"));
// });

var articles = {
    'article-one': {
      title : "Article One",
      heading : "Article-one-heading",
      content : "The text for article one",
    },
    'article-two': {
      title : "Article Two",
      heading : "Article-two-heading",
      content : "The text for article two",
    },
    'article-three': {
      title : "Article Three",
      heading : "Article-three-heading",
      content : "The text for article three",
    },
};

function templating(data) {
  var title = data.title;
  var heading = data.heading;
  var body = data.content;
  var html = `<html>
    <head>
      <title>
        ${title}
      </title>
      <link href="/ui/style.css" rel="stylesheet" />
      <link href="/ui/css/navBarTest.css" rel="stylesheet" />
      <ul>
        <li><a href="/">Home</a></li>
        <li class="dropdown">
          <a href="javascript:void(0)" class="dropbtn">Articles</a>
          <div class="dropdown-content">
            <a href="/article/article-one">Article One</a>
            <a href="/article/article-two">Article Two</a>
            <a href="/article/article-three">Article Three</a>
          </div>
        </li>
      </ul>
    </head>
    <body>
          <div class="container">
              <h1>
                ${heading}
              </h1>
              <hr>
              ${body}
          </div>
    </body>
  </html>`;
  return html;
};


var port = 8080; // Use 8080 for local development because you might already have apache running on 80
app.listen(8080, function () {
  console.log(`IMAD course app listening on port ${port}!`);
});
