# Secure Messaging Board - v1.0

## Introduction and data model

Secure Messaging Board (SMB) is an application for the _notarization_ of digital content -
typically, but not exclusively: messages, software objects, media - which are published by
an author and directed to a distribution list or specific recipient. SMB implements,
using Blockchain technology, a security process that allows you to store a
contained in a shared area (e.g. cloud storage), publishing simultaneously on the Blockchain
a record that announces its existence, provides a pointer to access it online and sets a
"Digital seal" to guarantee its origin and integrity. 
The structure of the record is as follows:

```
DOMAIN key optional string Distribution channel: domain
```
```
ENVIRONMENT key optional string Distribution channel: sub-domain
```
```
PROCESS key optional string Distribution channel: process
NAME key string Object Name
```
```
VERSION key integer Object version
```
```
CREATED data/time Timestamp of publication
```
```
CREATED_BY string Identity of the user who publishes
```
```
SIGNED_BY optional URL Identity of signer (if the content is
digitally signed)
SEAL string Hash of content
CONFIDENTIAL_FOR optional URL Identity of the recipient (if the content
is encrypted with a public key)
MESSAGE_REF URL Object pointer
```
```
MESSAGE_SIZE decimal Object dimension (MB)
```
As you can see, the record has a composite key, whose first elements (DOMAIN,
ENVIRONMENT and PROCESS) identify a distribution channel - which can be omitted - while
the last ones (NAME and VERSION) represent the logical name of the published object and, if any
expected, its progressive version number.

MESSAGE_REF is a URL that identifies the object published in the common storage area, e
allows you to download a copy; MESSAGE_SIZE indicates its size. Note that the area of
storage is not part of SMB, and must therefore be managed by users (including control
of accesses, when required).

The SEAL field contains a _hash_ value calculated on the content of the published object: it allows a
anyone who has received the object to verify it, applying the same algorithm already used in
publication phase, correspondence with the original content.

SIGNED_BY and CONFIDENTIAL_FOR respectively declare the user who has digitally signed
the object and its confidential recipient, if any. In both cases, the URL identifies a record,
belonging to any _public key infrastructure_, which establishes the identity (and its key
public) of the subject in question. As these are advanced features, they are not described in
this document.

## Access and profiling

For a user to take advantage of SMB, their identity must be registered in the system by the
platform manager, who then issues personal credentials to be used
for access.The credentials consist of a digital certificate that contains the user's identity e
his profile. In the section on client installation, it will be explained how to configure the
software with the credentials obtained from the manager. In this section, we give a brief explanation
of the profiling mechanism.

The profile is determined by two elements: role and scope. The role can take the value "reader" or
"writer". A user with the "reader" role can only perform read access, while the "writer" role
it also enables write access - that is, it allows the publication of objects on SMB.The scope
instead it defines individual operational limits based on the value of the DOMAIN fields,
ENVIRONMENT, PROCESS and NAME. To take a simple example, a profile defined as
"DOMAIN = 'domain ABC', ENVIRONMENT = 'ZXY environment'" limits the interested user to read /
write SMB records with these characteristics.Individual profile fields can also contain a
list of values, or be empty: in the second case, no operational limits are imposed.

## Operations

The center of the SMB application is a _smart contract_ Blockchain. For the convenience of users, this
version of the application is distributed together with a _client_ that offers a simple interface
interactive on the command line and makes the interaction with the Blockchain platform transparent, in
particularly with regard to communication protocols and security. At the same time, the
client integrates the basic functions for access, in reading and writing, to a common area of
storage based on the Google Drive service.
The combination of smart contract and client offers users three features:

- POST (only users with "writer" role): publish a new object, or a new version of
    an object already published. The logical name must be specified
    of the object (NAME), and optional the channel (DOMAIN, ENVIRONMENT e PROCESS).
    Additionally, the local disk path to a file representing the
    content associated with the object. If the operation is successful, upload the
    contained in the common storage area. If there is not already a record with the same
    combination of DOMAIN, ENVIRONMENT, PROCESS and NAME, the VERSION field comes
    assigned the value 0 (zero); otherwise, it receives the VERSION value of the previous record
    incremented by 1. All fields not included in the key are evaluated
    automatically.


- GET: retrieves the contents of an object, specified by the key. If he comes
    specified a version number, the content returned matches the version indicated;
    otherwise, it is that of the latest published version. The operation reports an error if
    it is not possible to access the indicated object, or if the content of the object has been
    altered after publication (check against the SEAL field).
- VERSION:retrieves the number of the last published version of an object, specified
    through the partial key (DOMAIN, ENVIRONMENT, PROCESS and NAME).

## Client installation

**System requirements**

- Java JRE 1.8 or higher
- Internet connection without proxy
- Account Google Drive

**Google Drive Integration**
In this version of the application, the only service supported as a common area of
storage is Google Drive. During the first use, by performing a POST operation,
the user is invited to access a specific web page to manage the consent to the use of
data, necessary to use the service. Once consent has been provided, it will no longer be required in
following.

**Installation**
The client is distributed, on individual request, by the platform manager. It consists of two
file: a JAR (executable) containing the software and a ZIP (wallet) that encapsulates the whole
configuration, including network addresses and login credentials.

The executable is the same - with the same software version - for all users. Being a
Java program, it can be copied by the user to any folder on your disk, from which it will come
then launched with the “java” command (see Use of the client).

The wallet, on the other hand, is strictly personal. The ZIP file must be unzipped by the user on his own
disk - again, in a folder of your choice - and its path passed as an argument
of the “java” command (see Using the client). If a user receives multiple distinct identities from the manager
(typically, to access with different profiles), it will install a single executable and multiple wallets,
unzipped each in its own folder; you can then specify which one during execution
profile use to perform the operation.


## Use of the client

The client is not interactive: to perform a single operation, it must be launched from the
system with a specific command. The command is structured as follows (it is assumed that the Java JRE has been
configured correctly):

_java - jar <path of file executable>_

- w <path absolute of folder wallet> (mandatory)
- o <operation: POST, GET o VERSION> (mandatory)
- f <POST: absolute path of the file that represents the object to be published; GET absolute path of the file in which to copy the contents of the object to be recovered>
- d <argument DOMAIN> (optional)
- e <argument ENVIRONMENT> (optional)
- p <argument PROCESS> (optional)
- n <argument NAME> (mandatory)
- v <GET: argomento VERSION> (optional)

At each execution, the related log (activity and any errors) is added to the end of the smb-
ledger.log, located in the logs sub-folder.

**Examples for PC Windows (mandatory arguments in bold)**

**_java -jar smb-ledger-1.0.0.jar - w C:\Users\mywindowsuser\smb\smbuser_**

**_- o POST_**
_- d mydomain - e myenv - p myprocess_
**_- n myobject - f C:\Users\mywindowsuser\Documents\mycontent-myversion.bin_**

**_java -jar smb-ledger-1.0.0.jar -w C:\Users\mywindowsuser\smb\smbuser_**

**_- o GET_**
_- d mydomain -e myenv -p myprocess - v targetversion_
**_- n myobject - f C:\Users\mywindowsuser\MyFolder\mycontent.bin_**

**_java -jar smb-ledger-1.0.0.jar - w C:\Users\mywindowsuser\smb\smbuser_**

**_- o VERSION_**
_- d mydomain -e myenv -p myprocess_
**_- n myobject_**
