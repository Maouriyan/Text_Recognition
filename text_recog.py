import subprocess
import time
import os
import argparse
import re


ap = argparse.ArgumentParser()
ap.add_argument("-i", "--image", type=str,
	help="path to input image")
args = vars(ap.parse_args())
val = args["image"]
link = re.escape(val)
print(link)

subprocess.call("adb shell am force-stop com.poojab26.textrecognizer", shell=True, stdout=subprocess.PIPE, universal_newlines=True)
print("App Stopped")
#output = subprocess.run("adb shell am start -n com.poojab26.textrecognizer/.MainActivity -e name1 https\:\/\/practice\.typekit\.com\/social\/l002\-social\.jpg", shell=True, stdout=subprocess.PIPE, universal_newlines=True)
                        ##To Start the App and intent (Provide link after name1)
os.system('adb logcat -b all -c')
os.system("adb shell am start -n com.poojab26.textrecognizer/.MainActivity -e name1 "+link)                       
print("Run Intent")                        
#print(output.stdout)
output = subprocess.run('timeout 3 adb logcat -s "TAG" -v raw', shell=True, stdout=subprocess.PIPE, universal_newlines=True)

                       
#output= os.system('timeout 5 adb logcat -s "TAG" -v raw')    
result = output.stdout                
print("RUn Logcat") 
#print(result)
post = result.split("\n",2)[2]
post = " ".join(post.splitlines())  
print("------------------------")  
print(post)           
