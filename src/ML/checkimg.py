import torch
from torchvision import transforms
from PIL import Image
import config
import sys

class CheckImage():
	def __init__(self):
		self.DEVICE = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
		
		model_path = config.model_path
		self.MODEL = torch.load(model_path, map_location=self.DEVICE)

		self.TRANSFORMS = transforms.Compose([
			transforms.ToTensor(),
			transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
			])
		
		self.CLASSES = [
			  'Нет аномалий',
			  'Подозрение на разливы'
		]
    

	def preprocess_img(self, img_path):
		img = Image.open(img_path)
		resized_img = img.resize((img.width // 5, img.height // 5), Image.ANTIALIAS)
		return self.TRANSFORMS(resized_img)
		

	def pred_image(self, img_path):
		inputs = self.preprocess_img(img_path).to(self.DEVICE)

		self.MODEL.eval()

		pred = self.MODEL(inputs.unsqueeze(0))

		argmax = pred.argmax(1).item()
		prob = pred.softmax(1)[0, argmax].item()

		cls_name = self.CLASSES[argmax]
		
		self.MODEL.train()

		print(cls_name, '\nВероятность: {}'.format(prob))
	
	
if __name__ == '__main__':
	chim = CheckImage()
	chim.pred_image(sys.argv[1])